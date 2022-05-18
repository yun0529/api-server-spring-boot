package com.example.demo.src.Kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.src.Chat.model.PostChat;
import com.example.demo.src.Kakao.model.KaKaoTokens;
import com.example.demo.src.Kakao.model.PostCreateKakaoAccountRes;
import com.example.demo.src.Kakao.model.PostKakaoJwt;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
@RequestMapping("/kakao")
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
     * 카카오 인가 코드 발급 API
     * [GET] /kakao/login
     *
     */
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    @GetMapping("/login") // (GET) 127.0.0.1:9000/kakao/login
    public BaseResponse<KaKaoTokens> kakaoCallback(@RequestParam String code) throws BaseException {
        //int userIdxByJwt = jwtService.getUserIdx();
        KaKaoTokens tokens = kakaoService.getKaKaoAccessToken(code);

        KaKaoTokens result = tokens;
        return new BaseResponse<KaKaoTokens>(result);
    }

    /**
     * 카카오 로그인 연동 API
     * [POST] /kakao/connect
     *
     */
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    @PostMapping("/connect") // (POST) 127.0.0.1:9000/kakao/connect
    public BaseResponse<PostKakaoJwt> postKakaoConnect(@RequestParam String accessToken, @RequestParam String refreshToken) throws BaseException {
        //jwt에서 idx 추출.
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //String access_Token = kakaoService.getKaKaoAccessToken(code);
            //kakaoService.createKakaoUser(accessToken);
            //userIdx와 접근한 유저가 같은지 확인
            PostCreateKakaoAccountRes postCreateKakaoAccountRes = kakaoService.createKakaoUser(accessToken, refreshToken); // 나중에 안드로이드에서 확인해보기 질문도 해보기
            //String access_Token = kakaoProvider.postKakaoConnect(userIdxByJwt);
            PostKakaoJwt postKakaoJwt = kakaoProvider.postKakaoConnect(userIdxByJwt, postCreateKakaoAccountRes.getKakaoId());
            return new BaseResponse<>(postKakaoJwt);
        }catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 카카오 로그아웃 API
     * [PATCH] /kakao/logout
     *
     */
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    @PatchMapping("/logout") // (PATCH) 127.0.0.1:9000/kakao/logout
    public BaseResponse<String> postKakaoConnect(@RequestParam String accessToken) throws BaseException {
        //jwt에서 idx 추출.
        try {
            Long kakaoIdByJwt = jwtService.getKakaoId();
            BigInteger kakaoId = BigInteger.valueOf(kakaoIdByJwt);
            System.out.println("jwt까진 받아옴 : " + kakaoId);
            kakaoService.patchKaKaoAccountInactive(kakaoId, accessToken);
            String result = "";
            return new BaseResponse<>(result);
        }catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
