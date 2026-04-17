package com.neogulss.neogulmap.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

public class SalesPredDTO {

    /**
     * 프론트 요청 DTO
     * 현재 ReportDTO.Request를 계속 쓸 거면 이 클래스는 없어도 됨
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesUserRequest {
        private Integer adminDongCode;
        private String serviceIndustryCode;
    }

    /**
     * DB 조회 결과 DTO
     * REPORT_DATA_COMMON + REPORT_DATA_SALES JOIN 결과
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesInput {
        private Integer baseYearQuarterCode;
        private Integer adminDongCode;
        private String serviceIndustryCode;
        private Integer quarterCode;
        private Integer operatingStoreCount;
        private Integer totalOperatingStoreCount;
        private Integer areaSize;
        private Float operatingFranchiseStoreRatio;
        private Float competitionDensity;
        private Float floatingPopTotalLog;
        private Float floatingPopPerStore;
        private Float youngPopRatio;
        private Float weekendPopRatio;
        private Integer avgMonthlyIncome;
        private Float foodExpenditureRatio;
        private Float entertainmentExpenditureRatio;
        private Float educationExpenditureRatio;
        private Float leisureExpenditureRatio;
        private Integer totalStoreCountChange;
        private Integer floatingPopChange;
        private Float closureRateChange;

        private Float salesLag1Log;
        private Float salesLag2Log;
        private Float salesLag3Log;
        private Float salesLag4Log;
        private Float salesMa2;
        private Float salesMa3;
        private Float salesStd2;
        private Float salesStd3;
        private Float salesGrowthRate;
        private Float salesToMa3Ratio;
        private Float salesChangeRate;
        private Float salesToIndustryAvgRatio;
        private Float floatingPopDensity;
        private Integer totalStoreCountLag1;
        private Integer totalPopLag1;
        private Float competitionRatio;
    }

    /**
     * FastAPI 요청 DTO
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesApiRequest {

        @JsonProperty("기준_분기_코드")
        private Integer quarterCode;

        @JsonProperty("행정동_코드")
        private Integer adminDongCode;

        @JsonProperty("서비스_업종_코드")
        private String serviceIndustryCode;

        @JsonProperty("sales_lag1_log")
        private Float salesLag1Log;

        @JsonProperty("sales_lag2_log")
        private Float salesLag2Log;

        @JsonProperty("sales_lag3_log")
        private Float salesLag3Log;

        @JsonProperty("sales_lag4_log")
        private Float salesLag4Log;

        @JsonProperty("sales_ma2")
        private Float salesMa2;

        @JsonProperty("sales_ma3")
        private Float salesMa3;

        @JsonProperty("sales_std2")
        private Float salesStd2;

        @JsonProperty("sales_std3")
        private Float salesStd3;

        @JsonProperty("sales_growth_1q")
        private Float salesGrowthRate;

        @JsonProperty("sales_vs_ma3")
        private Float salesToMa3Ratio;

        @JsonProperty("매출_증감률")
        private Float salesChangeRate;

        @JsonProperty("매출_대비_업종평균_비율")
        private Float salesToIndustryAvgRatio;

        @JsonProperty("점포_수")
        private Integer operatingStoreCount;

        @JsonProperty("유사_업종_점포_수")
        private Integer totalOperatingStoreCount;

        @JsonProperty("franchise_ratio")
        private Float operatingFranchiseStoreRatio;

        @JsonProperty("경쟁_밀도")
        private Float competitionDensity;

        @JsonProperty("competition_ratio")
        private Float competitionRatio;

        @JsonProperty("영역_면적")
        private Integer areaSize;

        @JsonProperty("floating_population_per_store")
        private Float floatingPopPerStore;

        @JsonProperty("유동인구_밀도")
        private Float floatingPopDensity;

        @JsonProperty("young_pop_ratio")
        private Float youngPopRatio;

        @JsonProperty("weekend_pop_ratio")
        private Float weekendPopRatio;

        @JsonProperty("log_총_유동인구_수")
        private Float floatingPopTotalLog;

        @JsonProperty("월_평균_소득_금액")
        private Integer avgMonthlyIncome;

        @JsonProperty("음식_지출_비율")
        private Float foodExpenditureRatio;

        @JsonProperty("유흥_지출_비율")
        private Float entertainmentExpenditureRatio;

        @JsonProperty("교육_지출_비율")
        private Float educationExpenditureRatio;

        @JsonProperty("여가문화_지출_비율")
        private Float leisureExpenditureRatio;

        @JsonProperty("유사_업종_점포_수_lag1")
        private Integer totalStoreCountLag1;

        @JsonProperty("유사_업종_점포_수_변화_량")
        private Integer totalStoreCountChange;

        @JsonProperty("폐업_률_변화_량")
        private Float closureRateChange;

        @JsonProperty("총_유동인구_수_lag1")
        private Integer totalPopLag1;

        @JsonProperty("유동인구_변화_량")
        private Integer floatingPopChange;

        public static SalesApiRequest from(SalesInput input) {
            return SalesApiRequest.builder()
                    .quarterCode(input.getQuarterCode())
                    .adminDongCode(input.getAdminDongCode())
                    .serviceIndustryCode(input.getServiceIndustryCode())
                    .salesLag1Log(input.getSalesLag1Log())
                    .salesLag2Log(input.getSalesLag2Log())
                    .salesLag3Log(input.getSalesLag3Log())
                    .salesLag4Log(input.getSalesLag4Log())
                    .salesMa2(input.getSalesMa2())
                    .salesMa3(input.getSalesMa3())
                    .salesStd2(input.getSalesStd2())
                    .salesStd3(input.getSalesStd3())
                    .salesGrowthRate(input.getSalesGrowthRate())
                    .salesToMa3Ratio(input.getSalesToMa3Ratio())
                    .salesChangeRate(input.getSalesChangeRate())
                    .salesToIndustryAvgRatio(input.getSalesToIndustryAvgRatio())
                    .operatingStoreCount(input.getOperatingStoreCount())
                    .totalOperatingStoreCount(input.getTotalOperatingStoreCount())
                    .operatingFranchiseStoreRatio(input.getOperatingFranchiseStoreRatio())
                    .competitionDensity(input.getCompetitionDensity())
                    .competitionRatio(input.getCompetitionRatio())
                    .areaSize(input.getAreaSize())
                    .floatingPopPerStore(input.getFloatingPopPerStore())
                    .floatingPopDensity(input.getFloatingPopDensity())
                    .youngPopRatio(input.getYoungPopRatio())
                    .weekendPopRatio(input.getWeekendPopRatio())
                    .floatingPopTotalLog(input.getFloatingPopTotalLog())
                    .avgMonthlyIncome(input.getAvgMonthlyIncome())
                    .foodExpenditureRatio(input.getFoodExpenditureRatio())
                    .entertainmentExpenditureRatio(input.getEntertainmentExpenditureRatio())
                    .educationExpenditureRatio(input.getEducationExpenditureRatio())
                    .leisureExpenditureRatio(input.getLeisureExpenditureRatio())
                    .totalStoreCountLag1(input.getTotalStoreCountLag1())
                    .totalStoreCountChange(input.getTotalStoreCountChange())
                    .closureRateChange(input.getClosureRateChange())
                    .totalPopLag1(input.getTotalPopLag1())
                    .floatingPopChange(input.getFloatingPopChange())
                    .build();
        }
    }

    /**
     * FastAPI 응답 DTO
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesApiResponse {

        @JsonProperty("pred_sales")
        private Long predSales;

        @JsonProperty("segment")
        private Integer segment;

        @JsonProperty("confidence")
        private String confidence;

        @JsonProperty("top_sales_factors")
        private List<TopSalesFactor> topSalesFactors;

        @JsonProperty("message")
        private String message;
    }

    /**
     * FastAPI 응답 내부 top factor DTO
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TopSalesFactor {
        private String feature;

        @JsonProperty("feature_value")
        private Object featureValue;

        private BigDecimal impact;
        private String direction;
    }

    /**
     * DB 저장용 DTO
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SalesOutput {
        private Integer baseYearQuarterCode;
        private Integer predYearQuarterCode;
        private Integer adminDongCode;
        private String serviceIndustryCode;
        private Long predSalesPerStore;
        private Integer segment;
        private String confidence;
        private String topSalesFactors;
        private String message;
        @JsonIgnore
        private String salesAiResponse;
        private String aiComment;
    }
}
