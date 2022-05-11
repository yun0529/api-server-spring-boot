package com.example.demo.src.Chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRoom {
    private int roomNo;
    private String userImageUrl;
    private String roomTitle;
    private String chatContext;
    private String updatedAt;
}
