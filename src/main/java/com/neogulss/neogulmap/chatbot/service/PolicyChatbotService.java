package com.neogulss.neogulmap.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.neogulss.neogulmap.chatbot.dto.*;
import com.neogulss.neogulmap.chatbot.entity.ChatLog;
import com.neogulss.neogulmap.chatbot.entity.ChatSession;
import com.neogulss.neogulmap.chatbot.entity.RagLog;
import com.neogulss.neogulmap.chatbot.repository.ChatLogRepository;
import com.neogulss.neogulmap.chatbot.repository.ChatSessionRepository;
import com.neogulss.neogulmap.chatbot.repository.RagLogRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neogulss.neogulmap.chatbot.dto.ChatSessionTitleResponse;
import com.neogulss.neogulmap.chatbot.dto.DeleteChatSessionResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyChatbotService {

    private final PolicyChatbotAiClient policyChatbotAiClient;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatLogRepository chatLogRepository;
    private final RagLogRepository ragLogRepository;
    private final ObjectMapper objectMapper;

    public PolicyChatbotService(
            PolicyChatbotAiClient policyChatbotAiClient,
            ChatSessionRepository chatSessionRepository,
            ChatLogRepository chatLogRepository,
            RagLogRepository ragLogRepository,
            ObjectMapper objectMapper
    ){
        this.policyChatbotAiClient = policyChatbotAiClient;
        this.chatSessionRepository = chatSessionRepository;
        this.chatLogRepository = chatLogRepository;
        this.ragLogRepository = ragLogRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PolicyChatbotSendResponse send(Long userIdx, PolicyChatbotSendRequest request){
        ChatSession session = resolveSession(userIdx, request.getSessionIdx());

        PolicyChatbotAiResponse aiResponse = policyChatbotAiClient.ask(request);

        if(session.getTitle() == null || session.getTitle().isBlank()){
            session.setTitle(aiResponse.getSessionTitleSuggestion());
        }

        ChatLog chatLog = new ChatLog();
        chatLog.setSession(session);
        chatLog.setContents(toJson(aiResponse.getChatLogPayload()));
        ChatLog savedChatLog = chatLogRepository.save(chatLog);

        RagLog ragLog =  new RagLog();
        ragLog.setChatLog(savedChatLog);
        ragLog.setRagLog(toJson(aiResponse.getRagLogPayload()));
        ragLogRepository.save(ragLog);

        return toFrontResponse(session, aiResponse);
    }

    @Transactional(readOnly = true)
    public List<ChatSessionListItemResponse> getSessions(Long userIdx){
        List<ChatSession> sessions = chatSessionRepository.findByUserIdxOrderByCreatedAtDesc(userIdx);

        List<ChatSessionListItemResponse> responses = new ArrayList<>();
        for(ChatSession session : sessions){
            ChatSessionListItemResponse item = new ChatSessionListItemResponse();
            item.setSessionIdx(session.getSessionIdx());
            item.setTitle(session.getTitle());
            item.setCreatedAt(session.getCreatedAt());
            responses.add(item);
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public List<ChatLogListItemResponse> getSessionLogs(Long userIdx, Long sessionIdx){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        List<ChatLog> chatLogs = chatLogRepository.findBySessionOrderByCreatedAtAsc(session);

        List<ChatLogListItemResponse> responses = new ArrayList<>();

        for(ChatLog chatLog : chatLogs){
            ChatLogListItemResponse item = new ChatLogListItemResponse();
            item.setChatLogIdx(chatLog.getChatLogIdx());
            item.setCreatedAt(chatLog.getCreatedAt());

            ChatLogContents chatContents = parseChatLogContents(chatLog.getContents());
            if (chatContents != null) {
                item.setUserQuery(chatContents.getUserQuery());
                item.setBotResponse(chatContents.getBotResponse());
                item.setModel(chatContents.getModel());
                item.setTurnLatencyMs(chatContents.getTurnLatencyMs());
            } else {
                item.setUserQuery("");
                item.setBotResponse("");
                item.setModel("");
                item.setTurnLatencyMs(0);
            }

            ragLogRepository.findByChatLog(chatLog).ifPresent(ragLog -> {
                ChatLogListItemResponse.RagLogItem ragItem = parseRagLog(ragLog.getRagLog());
                item.setRag(ragItem);
            });

            responses.add(item);
        }

        return responses;
    }

    @Transactional
    public ChatSessionTitleResponse updateSessionTitle(Long userIdx, Long sessionIdx,String title){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        session.setTitle(title);

        ChatSessionTitleResponse response = new ChatSessionTitleResponse();
        response.setSessionIdx(session.getSessionIdx());
        response.setTitle(session.getTitle());
        return response;
    }

    @Transactional
    public DeleteChatSessionResponse deleteSession(Long userIdx, Long sessionIdx){
        ChatSession session = chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));

        chatSessionRepository.delete(session);

        DeleteChatSessionResponse response = new DeleteChatSessionResponse();
        response.setSessionIdx(sessionIdx);
        response.setMessage("세션이 삭제되었습니다.");
        return response;
    }

    private ChatSession resolveSession(Long userIdx, Long sessionIdx){
        if(sessionIdx == null){
            ChatSession session = new ChatSession();
            session.setUserIdx(userIdx);
            session.setTitle(null);
            return chatSessionRepository.save(session);
        }

        return chatSessionRepository.findBySessionIdxAndUserIdx(sessionIdx, userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않거나 접근할 수 없는 세션입니다."));
    }

    private PolicyChatbotSendResponse toFrontResponse(ChatSession session, PolicyChatbotAiResponse aiResponse){
        PolicyChatbotSendResponse response = new PolicyChatbotSendResponse();
        response.setSessionIdx(session.getSessionIdx());
        response.setSessionTitle(session.getTitle());
        response.setType(aiResponse.getType());
        response.setAnswer(aiResponse.getAnswer());

        List<PolicyChatbotSendResponse.ReferenceItem> refs = new ArrayList<>();

        if (aiResponse.getRetrievedDocuments() != null) {
            for(PolicyChatbotAiResponse.RetrievedDocumentItem item : aiResponse.getRetrievedDocuments()){
                PolicyChatbotSendResponse.ReferenceItem ref = new PolicyChatbotSendResponse.ReferenceItem();
                ref.setSource(item.getSource());
                ref.setChunkText(item.getChunkText());
                ref.setFaissScore(item.getFaissScore());
                ref.setBm25Score(item.getBm25Score());
                ref.setRerankScore(item.getRerankScore());
                refs.add(ref);
            }
        }

        response.setReferences(refs);
        return response;
    }

    private String toJson(Object value){
        try{
            return objectMapper.writeValueAsString(value);
        }catch(JsonProcessingException e){
            throw new IllegalStateException("JSON 직렬화 중 오류가 발생했습니다.", e);
        }
    }

    private ChatLogContents parseChatLogContents(String json) {
        try {
            if (json == null || json.isBlank() || "null".equals(json.trim())) {
                return null;
            }
            return objectMapper.readValue(json, ChatLogContents.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("CHAT_LOG CONTENTS 파싱 중 오류가 발생했습니다.", e);
        }
    }

    private ChatLogListItemResponse.RagLogItem parseRagLog(String json){
        try{
            return objectMapper.readValue(json, ChatLogListItemResponse.RagLogItem.class);
        }catch(JsonProcessingException e){
            throw new IllegalStateException("RAG_LOG 파싱 중 오류가 발생했습니다.", e);
        }
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatLogContents{
        private String userQuery;
        private String botResponse;
        private String model;
        private Integer turnLatencyMs;
    }
}
