package com.example.demo.src.Alarm;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.product.model.PostInterestReq;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DO_LOGIN;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

// Service Create, Update, Delete 의 로직 처리
@Service
public class AlarmService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AlarmDao alarmDao;
    private final AlarmProvider alarmProvider;
    private final JwtService jwtService;
    private final UserDao userDao;
    @Autowired
    public AlarmService(AlarmDao alarmDao, AlarmProvider alarmProvider, JwtService jwtService, UserDao userDao) {
        this.alarmDao = alarmDao;
        this.alarmProvider = alarmProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void postKeyword(PostKeywordReq postKeywordReq) throws BaseException{
        User user = userDao.getNo(postKeywordReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            alarmDao.postKeyword(postKeywordReq);
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
