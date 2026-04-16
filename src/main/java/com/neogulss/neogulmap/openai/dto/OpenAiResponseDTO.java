package com.neogulss.neogulmap.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiResponseDTO {

    private String id;
    private String model;
    private String status;

    @JsonProperty("output_text")
    private String outputText;

    private List<OutputItem> output;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputItem {
        private String id;
        private String type;
        private String role;
        private List<OutputContent> content;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputContent {
        private String type;
        private String text;
    }

    public String extractText() {
        if (outputText != null && !outputText.isBlank()) {
            return outputText;
        }
        if (output == null || output.isEmpty()) {
            return "";
        }

        List<String> texts = new ArrayList<>();
        for (OutputItem item : output) {
            if (item.getContent() == null) continue;
            for (OutputContent c : item.getContent()) {
                if (c.getText() != null && !c.getText().isBlank()) {
                    texts.add(c.getText());
                }
            }
        }
        return String.join("\n", texts);
    }
}