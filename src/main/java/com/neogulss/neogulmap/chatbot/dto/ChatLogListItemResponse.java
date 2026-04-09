package com.neogulss.neogulmap.chatbot.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatLogListItemResponse {

    private Long chatLogIdx;
    private LocalDateTime createdAt;
    private String userQuery;
    private String botResponse;
    private String model;
    private Integer turnLatencyMs;
    private RagLogItem rag;


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RagLogItem {
        private List<RetrievedDocumentItem> retrievedDocuments;
        private SystemLatencyItem systemLatency;
    }

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
    public static class SystemLatencyItem {
        private Integer retrievalMs;
        private Integer llmGenerationMs;
    }

}
