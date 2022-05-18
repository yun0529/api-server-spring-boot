package com.example.demo.src.Kakao;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.src.Kakao.model.PostCreateKakaoAccountRes;
import com.example.demo.src.Kakao.model.PostKakaoJwt;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;

//Provider : Read의 비즈니스 로직 처리
@Service
public class KakaoProvider {

    private final KakaoDao kakaoDao;
    private final JwtService jwtService;
    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public KakaoProvider(KakaoDao kakaoDao, JwtService jwtService, UserDao userDao) {
        this.kakaoDao = kakaoDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    public PostKakaoJwt postKakaoConnect(int userNo, BigInteger kakaoId) throws BaseException {
        GetUserRes users = userDao.getUser(userNo);
        if(users.getStatus().equals("Active")){
            throw new BaseException(ALREADY_LOGIN);
        }
        PostCreateKakaoAccountRes postCreateKakaoAccountRes = kakaoDao.getId(kakaoId);
        if(postCreateKakaoAccountRes.getStatus().equals("Active")){
            throw new BaseException(ALREADY_KAKAO_LOGIN);
        }
        // 카카오 jwt 발급
        String jwt = jwtService.createKakaoJwt(userNo, kakaoId);

        if(kakaoDao.checkKakaoJwt(userNo) ==1) {
            int result = kakaoDao.modifyKakaoJwt(userNo, jwt);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_KAKAO_JWT);
            }
            PostKakaoJwt postKakaoJwt = new PostKakaoJwt(userNo, jwt);
            return postKakaoJwt;
        }
        else{
            try {
                kakaoDao.postKakaoJwt(userNo, jwt);
                PostKakaoJwt postKakaoJwt = new PostKakaoJwt(userNo, jwt);
                return postKakaoJwt;
            }catch(Exception e){
                System.out.println(e);
                throw new BaseException(DATABASE_ERROR);
            }
        }

    }
}
