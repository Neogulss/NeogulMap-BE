package com.neogulss.neogulmap.chatbot.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PolicyChatbotAiResponse {

    private String type;
    private String answer;
    private String model;
    private String sessionTitleSuggestion;
    private List<RetrievedDocumentItem> retrievedDocuments;
    private LatencyItem latency;
    private ChatLogPayload chatLogPayload;
    private RagLogPayload ragLogPayload;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RetrievedDocumentItem {
        private String source;
        private String chunkText;
        private Double faissScore;
        private Double bm25Score;
        private Double rerankScore;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LatencyItem {
        private Integer retrievalMs;
        private Integer llmGenerationMs;
        private Integer turnLatencyMs;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatLogPayload {
        private String userQuery;
        private String botResponse;
        private String model;
        private Integer turnLatencyMs;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RagSystemLatency {
        private Integer retrievalMs;
        private Integer llmGenerationMs;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RagLogPayload {
        private List<RetrievedDocumentItem> retrievedDocuments;
        private RagSystemLatency systemLatency;
    }
}
