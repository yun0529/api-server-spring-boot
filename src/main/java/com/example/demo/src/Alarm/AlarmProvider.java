package com.example.demo.src.Alarm;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class AlarmProvider {

    private final AlarmDao alarmDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AlarmProvider(AlarmDao alarmDao, JwtService jwtService) {
        this.alarmDao = alarmDao;
        this.jwtService = jwtService;
    }

    public List<GetKeyword> getKeyword(int userNo) throws BaseException{
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
