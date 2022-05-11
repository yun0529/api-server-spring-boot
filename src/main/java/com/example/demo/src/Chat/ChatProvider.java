package com.example.demo.src.Chat;


import com.example.demo.config.BaseException;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ChatProvider {

    private final ChatDao chatDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChatProvider(ChatDao chatDao, JwtService jwtService) {
        this.chatDao = chatDao;
        this.jwtService = jwtService;
    }

    public List<GetKeyword> getKeyword(int userNo) throws BaseException{
        try{
            List<GetKeyword> getKeyword = chatDao.getKeyword(userNo);
            return getKeyword;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetChatRoom> getChatRoom(int userNo) throws BaseException{
        try{
            List<GetChatRoom> getChatRoom = chatDao.getChatRoom(userNo);
            return getChatRoom;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetChatContent> getChatContent(int roomNo) throws BaseException{
        try{
            List<GetChatContent> getChatContent = chatDao.getChatContent(roomNo);
            return getChatContent;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
