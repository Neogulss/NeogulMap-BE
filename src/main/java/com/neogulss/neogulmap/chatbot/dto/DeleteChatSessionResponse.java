package com.neogulss.neogulmap.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteChatSessionResponse {

    private Long sessionIdx;
    private String message;

}
