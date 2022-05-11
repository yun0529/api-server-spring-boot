package com.example.demo.src.Chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatContent {
    private int roomNo;
    private int writeUserNo;
    private int receiveUserNo;
    private String writeUserNickname;
    private String chatDate;
    private String chatTime;
}
