package com.neogulss.neogulmap.openai.dto;

import lombok.*;

/**
 * responseDTO를 내부에서 변환(gpt->local)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiGenerateResponseDTO{
    // RISK
    private String text;
    private String rawJson;
}
