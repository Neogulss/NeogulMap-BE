package com.neogulss.neogulmap.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpinionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long adminDongCode;
        private String serviceIndustryCode;

        // 점포 안정성
        private Integer storeCount;
        private Integer openingStoreCount;
        private Integer closureStoreCount;
        private Double avgOperatingYears;
        private Integer storePrevQuarterDiff;

        // 업종분포 & 프랜차이즈
        private Double franchiseRatio;
        private Double foodRatio;
        private Double serviceRatio;
        private Double retailRatio;

        // 유동인구
        private Long totalFloatingPopulation;
        private Long floatingPrevQuarterDiff;
        private String peakFloatingHour;
        private String peakFloatingAgeGroup;
        private Double maleFloatingRatio;

        // 매출 트렌드
        private Long monthlySalesAmount;
        private Long salesPrevQuarterAmountDiff;
        private Float salesChangeRate;
        private Float salesToIndustryAvgRatio;
        private Long weekdaySalesAmount;
        private Long weekendSalesAmount;
        private String primarySalesAgeGroup;
        private String primarySalesGender;

        // AI 예측
        private Long predictedSalesPerStore;
        private Double riskClosureRate;

        // 상권변화지표
        private String commercialChangeIndicator;
        private Integer closedBusinessMonthAvg;
        private Integer seoulClosedBusinessMonthAvg;

        // 지역 소비력
        private Long monthlyAvgIncomeAmount;
        private Long totalExpenditureAmount;
        private Long foodServiceExpenditureAmount;

        // 입지 인프라
        private Integer totalVisitorFacilityCount;
        private Integer subwayStationCount;
        private Integer busStopCount;

        // 직장/상주인구 & 가구
        private Long totalWorkerPopulation;
        private Long totalResidentPopulation;
        private Integer totalHouseholdCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String opinion;
    }
}
