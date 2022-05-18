package com.example.demo.src.Kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateKakaoRes {
    private BigInteger kakaoId;
    private String accessToken;
    private String refreshToken;
}
