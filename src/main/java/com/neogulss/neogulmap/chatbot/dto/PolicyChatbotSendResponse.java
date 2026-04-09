package com.neogulss.neogulmap.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PolicyChatbotSendResponse {

    private Long sessionIdx;
    private String sessionTitle;
    private String type;
    private String answer;
    private List<ReferenceItem> references = new ArrayList<>();

    @Getter
    @Setter
    public static class ReferenceItem{
        private String source;
        private String chunkText;
        private Double faissScore;
        private Double bm25Score;
        private Double rerankScore;
    }
}
