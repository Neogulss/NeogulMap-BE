package com.neogulss.neogulmap.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogulss.neogulmap.chatbot.dto.PolicyChatbotAiResponse;
import com.neogulss.neogulmap.chatbot.dto.PolicyChatbotSendRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PolicyChatbotAiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String pythonAiBaseUrl;

    public PolicyChatbotAiClient(
            @Value("${python.ai.base-url}") String pythonAiBaseUrl,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.pythonAiBaseUrl = pythonAiBaseUrl;
    }

    public PolicyChatbotAiResponse ask(PolicyChatbotSendRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("user_query", request.getUserQuery());
        body.put("session_idx", request.getSessionIdx());

        if (request.getUserProfile() != null) {
            Map<String, Object> userProfile = new LinkedHashMap<>();
            userProfile.put("industry", request.getUserProfile().getIndustry());
            userProfile.put("age", request.getUserProfile().getAge());
            userProfile.put("has_business_registration", request.getUserProfile().getHasBusinessRegistration());
            userProfile.put("region", request.getUserProfile().getRegion());
            body.put("user_profile", userProfile);
        } else {
            body.put("user_profile", null);
        }

        try {
            String requestJson = objectMapper.writeValueAsString(body);

            System.out.println("=== Python Request URL ===");
            System.out.println(pythonAiBaseUrl + "/api/policy-chatbot/ask");

            System.out.println("=== Python Request JSON ===");
            System.out.println(requestJson);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<PolicyChatbotAiResponse> response = restTemplate.exchange(
                    pythonAiBaseUrl + "/api/policy-chatbot/ask",
                    HttpMethod.POST,
                    entity,
                    PolicyChatbotAiResponse.class
            );

            return response.getBody();

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Python 요청 JSON 직렬화 실패", e);
        }
    }
}
