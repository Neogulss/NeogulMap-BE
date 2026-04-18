package com.neogulss.neogulmap.openai.utils;

import com.neogulss.neogulmap.openai.PromptType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OpenAiPrompt {

    public String instructions(PromptType type){
        return switch (type){
            case RISK_SUMMARY -> readFile("data/risk_summary_v1.txt");
            case SALES_SUMMARY -> "너는 매출 분석가다. 데이터를 설명해라.";
        };
    }

    private String readFile(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("프롬프트 파일 로드 실패", e);
        }
    }

    public String input(PromptType type, Map<String, Object> data) {
        return switch (type) {
            case RISK_SUMMARY -> buildRiskInput(data);
            case SALES_SUMMARY -> "예상 매출 데이터 " + data;
        };
    }

    private String buildRiskInput(Map<String, Object> data) {
        return """
        다음은 폐업률 예측 결과입니다.

        finalClosureRate: %s

        주요 영향 요인:
        1) %s (값: %s, 영향도: %s, 방향: %s)
        2) %s (값: %s, 영향도: %s, 방향: %s)
        3) %s (값: %s, 영향도: %s, 방향: %s)
        """.formatted(
                data.get("finalClosureRate"),

                data.get("top1FeatureName"),
                data.get("top1FeatureValue"),
                data.get("top1Impact"),
                data.get("top1Direction"),

                data.get("top2FeatureName"),
                data.get("top2FeatureValue"),
                data.get("top2Impact"),
                data.get("top2Direction"),

                data.get("top3FeatureName"),
                data.get("top3FeatureValue"),
                data.get("top3Impact"),
                data.get("top3Direction")
        );
    }
}