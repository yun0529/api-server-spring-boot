package com.example.demo.src.Chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.src.Chat.model.PostChat;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ChatProvider chatProvider;
    @Autowired
    private final ChatService chatService;
    @Autowired
    private final JwtService jwtService;

    public ChatController(ChatProvider chatProvider, ChatService chatService, JwtService jwtService){
        this.chatProvider = chatProvider;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 채팅방 조회 API
     * [GET] /chats/room/{userNo}
     * @return BaseResponse<List<GetChatRoom>>
     */
   //Query String
    @ResponseBody
    @GetMapping("/room/{userNo}") // (GET) 127.0.0.1:9000/chats/room/:userNo
    public BaseResponse<List<GetChatRoom>> getChatRoom(@PathVariable("userNo") int userNo) {
        try{
            List<GetChatRoom> getChatRoom = chatProvider.getChatRoom(userNo);
                return new BaseResponse<>(getChatRoom);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 채팅방 내용 조회 API
     * [GET] /chats/{roomNo}
     * @return BaseResponse<List<GetChatContent>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{roomNo}") // (GET) 127.0.0.1:9000/chats/{roomNo}
    public BaseResponse<List<GetChatContent>> getChatContent(@PathVariable("roomNo") int roomNo) {
        try{
            List<GetChatContent> getChatContent = chatProvider.getChatContent(roomNo);
            return new BaseResponse<>(getChatContent);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 채팅 보내기 API
     * [POST] /chats/send
     * @return BaseResponse<String>
     */
    //Query String
    @ResponseBody
    @PostMapping("/send") // (POST) 127.0.0.1:9000/chats/send
    public BaseResponse<String> postKeyword(@RequestBody PostChat postChat) {
        try{
            String result = "";
            chatService.postChat(postChat);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

}
