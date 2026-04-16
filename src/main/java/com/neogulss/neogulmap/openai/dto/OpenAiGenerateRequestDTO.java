package com.neogulss.neogulmap.openai.dto;

import com.neogulss.neogulmap.openai.PromptType;
import lombok.*;

import java.util.Map;

/**
 * requestDTO를 내부에서 변환(local->gpt)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiGenerateRequestDTO {
    private PromptType promptType;
    private Map<String, Object> data;
}