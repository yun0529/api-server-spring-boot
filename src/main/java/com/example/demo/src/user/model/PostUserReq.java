package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userId;
    private String userPw;
    private int userRegionNo;
    private int userCode;
    private String userNickname;
}
