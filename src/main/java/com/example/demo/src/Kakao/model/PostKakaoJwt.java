package com.example.demo.src.Kakao.model;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostKakaoJwt {
    private int userNo;
    private String kakaoJwt;
}
