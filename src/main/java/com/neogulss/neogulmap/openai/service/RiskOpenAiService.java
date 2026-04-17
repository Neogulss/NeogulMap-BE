package com.neogulss.neogulmap.openai.service;

import com.neogulss.neogulmap.openai.utils.OpenAiPrompt;
import com.neogulss.neogulmap.openai.client.OpenAiClient;
import com.neogulss.neogulmap.openai.dto.OpenAiClientResponseDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiGenerateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskOpenAiService {

    private final OpenAiClient openAiClient;
    private final OpenAiPrompt prompt;

    /**
     * OpenAI 호출 서비스
     * @param request OpenAiGenerateRequestDTO
     * @return OpenAiGenerateResponseDTO
     */
    public OpenAiClientResponseDTO generate(OpenAiGenerateRequestDTO request) {
        return openAiClient.generate(
                prompt.instructions(request.getPromptType()),
                prompt.input(request.getPromptType(), request.getData())
        );
    }
}
