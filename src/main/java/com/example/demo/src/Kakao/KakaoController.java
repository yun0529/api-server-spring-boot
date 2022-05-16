package com.example.demo.src.Kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.src.Chat.model.PostChat;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
public class KakaoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final KakaoProvider kakaoProvider;
    @Autowired
    private final KakaoService kakaoService;
    @Autowired
    private final JwtService jwtService;

    public KakaoController(KakaoProvider kakaoProvider, KakaoService kakaoService, JwtService jwtService){
        this.kakaoProvider = kakaoProvider;
        this.kakaoService = kakaoService;
        this.jwtService = jwtService;
    }

    /**
     * 카카오 소셜 로그인 API
     * [GET] /users/login/kakao
     *
     */
    @ResponseBody
    @GetMapping("/kakao/login") // (GET) 127.0.0.1:9000/users/login/kakao
    public BaseResponse<String> kakaoCallback(@RequestParam String code) throws BaseException {
        String access_Token = kakaoService.getKaKaoAccessToken(code);
        kakaoService.createKakaoUser(access_Token);
        System.out.println("token : " + code);
        String result = code;
        return new BaseResponse<>(result);
    }
}
