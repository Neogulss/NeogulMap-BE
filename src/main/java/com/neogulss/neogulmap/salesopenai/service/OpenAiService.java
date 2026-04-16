package com.neogulss.neogulmap.salesopenai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.neogulss.neogulmap.salesopenai.client.OpenAiClient;
import com.neogulss.neogulmap.salesopenai.dto.OpenAiDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiClient openAiClient;

    public OpenAiDTO.TextResponse ask(String prompt) {
        JsonNode response = openAiClient.createResponse(prompt);
        return OpenAiDTO.TextResponse.builder()
                .model(response.path("model").asText(openAiClient.getOpenAiModel()))
                .text(extractText(response))
                .build();
    }

    public String getOpenAiModel() {
        return openAiClient.getOpenAiModel();
    }

    private String extractText(JsonNode response) {
        JsonNode outputText = response.path("output_text");
        if (outputText.isTextual() && !outputText.asText().isBlank()) {
            return outputText.asText();
        }

        StringBuilder builder = new StringBuilder();
        JsonNode outputs = response.path("output");
        if (outputs.isArray()) {
            for (JsonNode output : outputs) {
                JsonNode contents = output.path("content");
                if (!contents.isArray()) {
                    continue;
                }

                for (JsonNode content : contents) {
                    JsonNode textNode = content.path("text");
                    if (textNode.isTextual() && !textNode.asText().isBlank()) {
                        if (builder.length() > 0) {
                            builder.append(System.lineSeparator());
                        }
                        builder.append(textNode.asText());
                    }
                }
            }
        }

        if (builder.length() > 0) {
            return builder.toString();
        }

        throw new IllegalStateException("OpenAI 텍스트 응답 파싱에 실패했습니다.");
    }
}
