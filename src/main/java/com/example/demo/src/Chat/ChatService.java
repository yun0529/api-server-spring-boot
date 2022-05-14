package com.example.demo.src.Chat;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.Chat.model.PostChat;
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
public class ChatService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatDao chatDao;
    private final ChatProvider chatProvider;
    private final JwtService jwtService;
    private final UserDao userDao;
    @Autowired
    public ChatService(ChatDao chatDao, ChatProvider chatProvider, JwtService jwtService, UserDao userDao) {
        this.chatDao = chatDao;
        this.chatProvider = chatProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void postChat(PostChat postChat) throws BaseException {
        User user = userDao.getNo(postChat.getWriteUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            chatDao.postChat(postChat);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
