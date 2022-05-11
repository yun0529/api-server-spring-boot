package com.example.demo.src.Alarm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostKeywordReq {
    private int userNo;
    private String keyword;
}
