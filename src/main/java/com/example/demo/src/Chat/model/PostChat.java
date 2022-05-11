package com.example.demo.src.Chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChat {
    private int roomNo;
    private int writeUserNo;
    private int receiveUserNo;
    private String chatContext;
}
