package com.neogulss.neogulmap.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.openai.dto.OpenAiGenerateRequestDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiGenerateResponseDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiRequestDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OpenAiPrompt prompt;
    private final ObjectMapper objectMapper;

    @Value("${openai.base-url}") private String baseUrl;
    @Value("${openai.api-key}") private String apiKey;
    @Value("${openai.model}") private String model;

    /**
     * OpenAI 호출 서비스
     * @param request OpenAiGenerateRequestDTO
     * @return OpenAiGenerateResponseDTO
     */
    public OpenAiGenerateResponseDTO generate(OpenAiGenerateRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        OpenAiRequestDTO requestBody = OpenAiRequestDTO.builder()
            .model(model)
            .instructions(prompt.instructions(request.getPromptType()))
            .input(List.of(
                OpenAiRequestDTO.InputItem.builder()
                    .role("user")
                    .content(List.of(
                        OpenAiRequestDTO.ContentItem.builder()
                            .type("input_text")
                            .text(prompt.input(request.getPromptType(), request.getData()))
                            .build()
                    ))
                    .build()
            ))
            .build();

        HttpEntity<OpenAiRequestDTO> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OpenAiResponseDTO> response = restTemplate.exchange(
            baseUrl + "/responses",
            HttpMethod.POST,
            entity,
            OpenAiResponseDTO.class
        );

        OpenAiResponseDTO responseBody = response.getBody();
        if (responseBody == null) {
            return OpenAiGenerateResponseDTO.builder()
                .text("")
                .rawJson("{}")
                .build();
        }

        return OpenAiGenerateResponseDTO.builder()
            .text(responseBody.extractText())
            .rawJson(toJson(responseBody))
            .build();
    }

    /**
     * 응답 데이터 json 변환
     * @param value Object
     * @return String
     */
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("OpenAI 응답 JSON 직렬화에 실패했습니다.", e);
        }
    }
}
