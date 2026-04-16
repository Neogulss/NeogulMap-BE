package com.neogulss.neogulmap.salesopenai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.neogulss.neogulmap.salesopenai.dto.OpenAiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class OpenAiClient {

    private final RestTemplate restTemplate;
    private final String openAiBaseUrl;
    private final String openAiModel;
    private final String openAiApiKey;

    public OpenAiClient(
            @Value("${openai.base-url}") String openAiBaseUrl,
            @Value("${openai.model}") String openAiModel,
            @Value("${OPENAI_API_KEY:}") String openAiApiKey
    ) {
        this.restTemplate = new RestTemplate();
        this.openAiBaseUrl = trimTrailingSlash(openAiBaseUrl);
        this.openAiModel = openAiModel;
        this.openAiApiKey = openAiApiKey;
    }

    public JsonNode createResponse(String prompt) {
        validateApiKey();

        OpenAiDTO.ResponsesRequest request = OpenAiDTO.ResponsesRequest.builder()
                .model(openAiModel)
                .input(prompt)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));

        HttpEntity<OpenAiDTO.ResponsesRequest> entity = new HttpEntity<>(request, headers);

        try {
            log.info("OpenAI 요청 - model: [{}], promptLength: [{}]", openAiModel, prompt.length());
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    openAiBaseUrl + "/responses",
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );

            if (response.getBody() == null) {
                throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
            }

            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new IllegalStateException(
                    "OpenAI 호출 실패 - status: " + e.getStatusCode() + ", body: " + e.getResponseBodyAsString(),
                    e
            );
        }
    }

    public String getOpenAiModel() {
        return openAiModel;
    }

    private void validateApiKey() {
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY 환경변수가 설정되지 않았습니다.");
        }
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
