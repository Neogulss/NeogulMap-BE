package com.neogulss.neogulmap.openai;

import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class OpenAiPrompt {
    /**
     * OpenAI API 프롬프트
     * @param type PromptType
     * @return String
     */
    public String instructions(PromptType type){
        return switch (type){
            case RISK_SUMMARY -> "너는 폐업 리스크 분석가다. 데이터를 설명해라.";
            case SALES_SUMMARY -> "너는 매출 분석가다. 데이터를 설명해라.";
        };
    }

    /**
     * OpenAI API 입력 데이터
     * @param type PromptType
     * @param data Map<String, Object>
     * @return String
     */
    public String input(PromptType type, Map<String, Object> data) {
        return switch (type) {
            case RISK_SUMMARY -> "예상 폐업률 데이터"+data;
            case SALES_SUMMARY -> "예상 매출 데이터"+data;
        };
    }
}
