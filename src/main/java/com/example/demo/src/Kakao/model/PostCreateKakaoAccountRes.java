package com.example.demo.src.Kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateKakaoAccountRes {
    private BigInteger kakaoId;
    private String connectedAt;
    private Boolean hasEmail;
    private String email;
    private String status;
}
