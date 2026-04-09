package com.neogulss.neogulmap.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatSessionListItemResponse {

    private Long sessionIdx;
    private String title;
    private LocalDateTime createdAt;
}
