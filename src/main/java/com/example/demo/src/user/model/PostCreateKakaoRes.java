package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateKakaoRes {
    private int id;
    private String createdAt;
    private PostCreateKakaoAccountRes postCreateKakaoAccountRes;
}
