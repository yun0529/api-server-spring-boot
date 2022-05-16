package com.example.demo.src.Kakao;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
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


}
