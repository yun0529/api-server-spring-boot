package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByUserNo(int userNo) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByUserNo(userNo);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUser(int userNo) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userNo);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetMyCarrot getMyCarrot(int userNo) throws BaseException {
        try {
            GetMyCarrot getMyCarrot = userDao.getMyCarrotByUserNo(userNo);
            return getMyCarrot;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(String userId) throws BaseException{
        try{
            return userDao.checkUserId(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getUserPw());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getUserPw().equals(encryptPwd)){
            if(user.getStatus().equals("Active")){
                throw new BaseException(FAILED_TO_LOGIN_STATUS);
            }
            else{
                userDao.modifyUserStatusLogIn(postLoginReq);
                int userNo = user.getUserNo();
                String jwt = jwtService.createJwt(userNo);
                return new PostLoginRes(userNo,jwt);
            }
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }
}
