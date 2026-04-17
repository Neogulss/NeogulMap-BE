package com.neogulss.neogulmap.openai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.openai.dto.OpenAiClientResponseDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiRequestDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Value("${openai.base-url}")
    private String baseUrl;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public OpenAiClientResponseDTO generate(String inputText) {
        return generate("", inputText);
    }

    public OpenAiClientResponseDTO generate(String instructions, String inputText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        OpenAiRequestDTO requestBody = OpenAiRequestDTO.builder()
                .model(model)
                .instructions(instructions)
                .input(List.of(
                        OpenAiRequestDTO.InputItem.builder()
                                .role("user")
                                .content(List.of(
                                        OpenAiRequestDTO.ContentItem.builder()
                                                .type("input_text")
                                                .text(inputText)
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
            return OpenAiClientResponseDTO.builder()
                    .model(model)
                    .text("")
                    .rawJson("{}")
                    .build();
        }

        return OpenAiClientResponseDTO.builder()
                .model(responseBody.getModel() == null || responseBody.getModel().isBlank() ? model : responseBody.getModel())
                .text(responseBody.extractText())
                .rawJson(toJson(responseBody))
                .build();
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("OpenAI 응답 JSON 직렬화에 실패했습니다.", e);
        }
    }
}
