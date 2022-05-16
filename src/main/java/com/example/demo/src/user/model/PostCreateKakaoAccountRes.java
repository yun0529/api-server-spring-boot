package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateKakaoAccountRes {
    private Boolean hasEmail;
    private Boolean emailNeedsAgreement;
    private String email;
}
