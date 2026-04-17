package com.neogulss.neogulmap.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiClientResponseDTO {
    // SALES, RISK
    private String model;
    private String text;
    private String rawJson;
}
