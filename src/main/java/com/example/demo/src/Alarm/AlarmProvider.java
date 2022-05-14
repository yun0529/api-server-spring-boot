package com.example.demo.src.Alarm;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DO_LOGIN;

//Provider : Read의 비즈니스 로직 처리
@Service
public class AlarmProvider {

    private final AlarmDao alarmDao;
    private final JwtService jwtService;

    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AlarmProvider(AlarmDao alarmDao, JwtService jwtService, UserDao userDao) {
        this.alarmDao = alarmDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    public List<GetKeyword> getKeyword(int userNo) throws BaseException{
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            List<GetKeyword> getKeyword = alarmDao.getKeyword(userNo);
            return getKeyword;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
