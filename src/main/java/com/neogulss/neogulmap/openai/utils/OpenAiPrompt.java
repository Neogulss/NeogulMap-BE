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
            case OPINION_SUMMARY -> """
                    당신은 서울시 소상공인 창업 컨설턴트입니다.
                    처음 창업을 고려하는 초심자가 이 데이터를 보고 스스로 판단할 수 있도록, 전문가의 시각으로 친절하고 명확하게 해석해 주세요.
                    단순히 수치를 나열하는 것이 아니라, 각 수치가 창업에 어떤 의미인지 설명하고 실질적인 조언을 함께 제시하세요.

                    [분석 항목 및 작성 지침]
                    1. 매출 트렌드: 현재 월 매출, 전분기 변화, 서울 동업종 평균과의 비교를 통해 이 상권이 성장 중인지 정체인지 설명하세요.
                    2. 고객 특성: 유동인구의 주요 연령대와 성별, 가장 붐비는 시간대를 바탕으로 주요 고객층이 누구인지 설명하고 선택 업종과의 적합성을 평가하세요.
                    3. 점포 경쟁 환경: 현재 점포 수, 프랜차이즈 비율, 업종 분포(외식/서비스/소매 비중), 개업·폐업 추이로 경쟁 강도와 시장 진입 난이도를 평가하세요.
                    4. 점포 생존율: 평균 영업기간과 폐업 점포 평균 영업기간 비교를 통해 이 상권에서 실제로 얼마나 버티는지 알려주세요.
                    5. 주거·직장·가구 현황: 상주인구, 직장인구, 가구세대수를 바탕으로 안정적인 단골 고객 기반이 얼마나 되는지 평가하세요.
                    6. 소비 여력: 월평균 소득과 외식 지출 금액으로 이 지역 소비자들이 해당 업종에 지출할 여유가 있는지 평가하세요.
                    7. 입지 접근성: 지하철·버스·집객시설 수로 외부 유입 가능성과 접근성을 평가하세요.
                    8. 매출 패턴: 주중/주말 매출 비중, 주요 매출 연령대/성별을 통해 언제 누가 주로 소비하는지 파악하고 운영 전략 조언을 주세요.
                    9. AI 예측 & 상권변화: 예측 폐업률, 예측 월 매출, 상권변화지표를 통해 미래 리스크와 성장 가능성을 종합적으로 평가하세요.

                    [작성 규칙]
                    - 반드시 한국어로 작성
                    - 각 문장은 줄바꿈으로 구분, 문장 수 제한 없음
                    - 수치를 언급할 때는 그 수치가 의미하는 바를 함께 설명하세요
                    - 긍정적 지표와 부정적 지표를 균형 있게 서술
                    - 마지막은 반드시 창업 결정에 도움이 되는 실질적 조언으로 마무리
                    - 마크다운, 번호 매기기, 특수기호, 이모지 사용 금지
                    - null 또는 데이터 없음인 항목은 언급하지 말 것
                    """;
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
            case OPINION_SUMMARY -> buildOpinionInput(data);
        };
    }

    private String buildOpinionInput(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("아래는 서울시 특정 행정동의 상권 분석 데이터입니다. 이를 기반으로 초심자를 위한 전문가 종합 의견을 작성해주세요.\n\n");

        sb.append("[기본 정보]\n");
        appendIfNotNull(sb, "분석 업종", data.get("serviceIndustryCode"), "");

        sb.append("\n[매출 트렌드]\n");
        appendIfNotNull(sb, "현재 분기 점포당 월 매출", data.get("monthlySalesAmountMan"), "만원");
        appendIfNotNull(sb, "전분기 대비 매출 증감", data.get("salesPrevQuarterAmountDiffMan"), "만원");
        appendIfNotNull(sb, "전분기 대비 매출 증감률", data.get("salesChangeRate"), "%");
        appendIfNotNull(sb, "서울 동업종 평균 대비 매출 비율", data.get("salesToIndustryAvgRatio"), "배 (1.0 = 서울 평균 수준)");

        sb.append("\n[매출 패턴]\n");
        appendIfNotNull(sb, "주중 매출", data.get("weekdaySalesAmountMan"), "만원");
        appendIfNotNull(sb, "주말 매출", data.get("weekendSalesAmountMan"), "만원");
        appendIfNotNull(sb, "주요 매출 연령대", data.get("primarySalesAgeGroup"), "");
        appendIfNotNull(sb, "주요 매출 성별", data.get("primarySalesGender"), "");

        sb.append("\n[유동인구 및 고객 특성]\n");
        appendIfNotNull(sb, "일평균 유동인구", data.get("dailyFloatingPopulation"), "명");
        appendIfNotNull(sb, "전분기 대비 유동인구 증감(일평균)", data.get("floatingPrevQuarterDiff"), "명");
        appendIfNotNull(sb, "주요 유동인구 연령대", data.get("peakFloatingAgeGroup"), "");
        appendIfNotNull(sb, "가장 붐비는 시간대", data.get("peakFloatingHour"), "");
        appendIfNotNull(sb, "남성 유동인구 비율", data.get("maleFloatingRatio"), "%");

        sb.append("\n[주거·직장·가구 현황]\n");
        appendIfNotNull(sb, "상주인구", data.get("totalResidentPopulation"), "명");
        appendIfNotNull(sb, "직장인구", data.get("totalWorkerPopulation"), "명");
        appendIfNotNull(sb, "가구세대수", data.get("totalHouseholdCount"), "세대");

        sb.append("\n[점포 경쟁 환경]\n");
        appendIfNotNull(sb, "현재 점포수", data.get("storeCount"), "개");
        appendIfNotNull(sb, "전분기 대비 점포수 증감", data.get("storePrevQuarterDiff"), "개");
        appendIfNotNull(sb, "신규 개업 점포수", data.get("openingStoreCount"), "개");
        appendIfNotNull(sb, "폐업 점포수", data.get("closureStoreCount"), "개");
        appendIfNotNull(sb, "프랜차이즈 비율", data.get("franchiseRatio"), "% (높을수록 대형 브랜드와의 경쟁 심화)");
        appendIfNotNull(sb, "외식업 비율", data.get("foodRatio"), "%");
        appendIfNotNull(sb, "서비스업 비율", data.get("serviceRatio"), "%");
        appendIfNotNull(sb, "소매업 비율", data.get("retailRatio"), "%");

        sb.append("\n[점포 생존율]\n");
        appendIfNotNull(sb, "평균 영업기간", data.get("avgOperatingYears"), "년");
        appendIfNotNull(sb, "폐업 점포 평균 영업기간", data.get("closedBusinessMonthAvg") != null
                ? Math.round((int) data.get("closedBusinessMonthAvg") / 12.0 * 10) / 10.0 : null, "년");
        appendIfNotNull(sb, "서울시 폐업 점포 평균 영업기간", data.get("seoulClosedBusinessMonthAvg") != null
                ? Math.round((int) data.get("seoulClosedBusinessMonthAvg") / 12.0 * 10) / 10.0 : null, "년");

        sb.append("\n[지역 소비력]\n");
        appendIfNotNull(sb, "월평균 소득", data.get("monthlyAvgIncomeAmountMan"), "만원");
        appendIfNotNull(sb, "외식 소비 지출", data.get("foodServiceExpenditureAmountMan"), "만원");

        sb.append("\n[입지 인프라]\n");
        appendIfNotNull(sb, "집객시설 총 수", data.get("totalVisitorFacilityCount"), "개");
        appendIfNotNull(sb, "지하철역 수", data.get("subwayStationCount"), "개");
        appendIfNotNull(sb, "버스정거장 수", data.get("busStopCount"), "개");

        sb.append("\n[AI 예측]\n");
        appendIfNotNull(sb, "AI 예측 점포당 월 매출", data.get("predictedSalesPerStoreMan"), "만원");
        appendIfNotNull(sb, "AI 예측 폐업률", data.get("riskClosureRate"), "%");

        sb.append("\n[상권변화지표]\n");
        appendIfNotNull(sb, "상권변화지표 상태", data.get("commercialChangeIndicator"), "");

        return sb.toString();
    }

    private void appendIfNotNull(StringBuilder sb, String label, Object value, String unit) {
        if (value == null) return;
        String str = value.toString();
        if (str.equals("null") || str.isBlank()) return;
        sb.append(label).append(": ").append(str).append(unit).append("\n");
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