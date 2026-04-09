package com.neogulss.neogulmap.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChatSessionTitleRequest {

    @NotBlank(message = "세션 제목은 비어 있을 수 없습니다.")
    @Size(max = 100, message = "세션 제목은 100자 이하여야 합니다.")
    private String title;
}
