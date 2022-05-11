package com.example.demo.src.Alarm;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.product.model.PostInterestReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class AlarmService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AlarmDao alarmDao;
    private final AlarmProvider alarmProvider;
    private final JwtService jwtService;

    @Autowired
    public AlarmService(AlarmDao alarmDao, AlarmProvider alarmProvider, JwtService jwtService) {
        this.alarmDao = alarmDao;
        this.alarmProvider = alarmProvider;
        this.jwtService = jwtService;
    }

    public void postKeyword(PostKeywordReq postKeywordReq) throws BaseException{
        try{
            alarmDao.postKeyword(postKeywordReq);
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

   /* public void registInterest(PostInterestReq postInterestReq) throws BaseException {
        try{
            if(alarmProvider.checkInterestProduct(postInterestReq.getUserNo(), postInterestReq.getProductNo()) ==1){
                alarmDao.modifyProductInterest(postInterestReq);
            }
            else{
                alarmDao.registInterest(postInterestReq);
            }

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }*/

}
