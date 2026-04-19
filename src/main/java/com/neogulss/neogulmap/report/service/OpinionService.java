package com.neogulss.neogulmap.report.service;

import com.neogulss.neogulmap.openai.PromptType;
import com.neogulss.neogulmap.openai.dto.OpenAiClientResponseDTO;
import com.neogulss.neogulmap.openai.dto.OpenAiGenerateRequestDTO;
import com.neogulss.neogulmap.openai.service.RiskOpenAiService;
import com.neogulss.neogulmap.report.dto.OpinionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionService {

    private final RiskOpenAiService openAiService;

    public OpinionDTO.Response generateOpinion(OpinionDTO.Request req) {
        log.info("[{}-{}] 종합의견 생성 시작", req.getAdminDongCode(), req.getServiceIndustryCode());

        Map<String, Object> data = new LinkedHashMap<>();

        // 업종
        data.put("serviceIndustryCode", req.getServiceIndustryCode());

        // 점포 안정성
        data.put("storeCount", req.getStoreCount());
        data.put("openingStoreCount", req.getOpeningStoreCount());
        data.put("closureStoreCount", req.getClosureStoreCount());
        data.put("avgOperatingYears", req.getAvgOperatingYears());
        data.put("storePrevQuarterDiff", req.getStorePrevQuarterDiff());

        // 업종분포 & 프랜차이즈
        data.put("franchiseRatio", req.getFranchiseRatio());
        data.put("foodRatio", req.getFoodRatio());
        data.put("serviceRatio", req.getServiceRatio());
        data.put("retailRatio", req.getRetailRatio());

        // 유동인구
        data.put("dailyFloatingPopulation",
                req.getTotalFloatingPopulation() != null ? req.getTotalFloatingPopulation() / 91 : null);
        data.put("floatingPrevQuarterDiff",
                req.getFloatingPrevQuarterDiff() != null ? req.getFloatingPrevQuarterDiff() / 91 : null);
        data.put("peakFloatingHour", req.getPeakFloatingHour());
        data.put("peakFloatingAgeGroup", req.getPeakFloatingAgeGroup());
        data.put("maleFloatingRatio", req.getMaleFloatingRatio());

        // 매출 트렌드
        data.put("monthlySalesAmountMan",
                req.getMonthlySalesAmount() != null ? req.getMonthlySalesAmount() / 10000 : null);
        data.put("salesPrevQuarterAmountDiffMan",
                req.getSalesPrevQuarterAmountDiff() != null ? req.getSalesPrevQuarterAmountDiff() / 10000 : null);
        data.put("salesChangeRate", req.getSalesChangeRate());
        data.put("salesToIndustryAvgRatio", req.getSalesToIndustryAvgRatio());
        data.put("weekdaySalesAmountMan",
                req.getWeekdaySalesAmount() != null ? req.getWeekdaySalesAmount() / 10000 : null);
        data.put("weekendSalesAmountMan",
                req.getWeekendSalesAmount() != null ? req.getWeekendSalesAmount() / 10000 : null);
        data.put("primarySalesAgeGroup", req.getPrimarySalesAgeGroup());
        data.put("primarySalesGender", req.getPrimarySalesGender());

        // AI 예측
        data.put("predictedSalesPerStoreMan",
                req.getPredictedSalesPerStore() != null ? req.getPredictedSalesPerStore() / 10000 : null);
        data.put("riskClosureRate", req.getRiskClosureRate());

        // 상권변화지표
        data.put("commercialChangeIndicator", req.getCommercialChangeIndicator());
        data.put("closedBusinessMonthAvg", req.getClosedBusinessMonthAvg());
        data.put("seoulClosedBusinessMonthAvg", req.getSeoulClosedBusinessMonthAvg());

        // 지역 소비력
        data.put("monthlyAvgIncomeAmountMan",
                req.getMonthlyAvgIncomeAmount() != null ? req.getMonthlyAvgIncomeAmount() / 10000 : null);
        data.put("foodServiceExpenditureAmountMan",
                req.getFoodServiceExpenditureAmount() != null ? req.getFoodServiceExpenditureAmount() / 10000 : null);

        // 입지 인프라
        data.put("totalVisitorFacilityCount", req.getTotalVisitorFacilityCount());
        data.put("subwayStationCount", req.getSubwayStationCount());
        data.put("busStopCount", req.getBusStopCount());

        // 직장/상주인구 & 가구
        data.put("totalWorkerPopulation", req.getTotalWorkerPopulation());
        data.put("totalResidentPopulation", req.getTotalResidentPopulation());
        data.put("totalHouseholdCount", req.getTotalHouseholdCount());

        OpenAiClientResponseDTO aiResponse = openAiService.generate(
                OpenAiGenerateRequestDTO.builder()
                        .promptType(PromptType.OPINION_SUMMARY)
                        .data(data)
                        .build()
        );

        String opinion = (aiResponse != null && aiResponse.getText() != null && !aiResponse.getText().isBlank())
                ? aiResponse.getText()
                : "종합 의견을 생성하지 못했습니다.";

        return OpinionDTO.Response.builder().opinion(opinion).build();
    }
}
