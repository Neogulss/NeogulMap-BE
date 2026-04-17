package com.neogulss.neogulmap.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RiskDTO {
    /**
     * 폐업률 프론트 요청 DTO
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RiskUserRequest {
        private Integer adminDongCode;
        private String serviceIndustryCode;
    }

    /**
     * 폐업률 DB Input
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RiskInput {
        private Integer baseYearQuarterCode;
        private Integer quarterCode;
        private Integer adminDongCode;
        private String serviceIndustryCode;
        private String commercialChangeCode;
        private Integer operatingStoreCount;
        private Integer totalOperatingStoreCount;
        private Double openingRate;
        private Double operatingFranchiseStoreRatio;
        private Double competitionDensity;
        private Double areaSize;
        private Double avgMonthlyIncome;
        private Double foodExpenditureRatio;
        private Double entertainmentExpenditureRatio;
        private Double educationExpenditureRatio;
        private Double leisureExpenditureRatio;
        private Double floatingPopTotalLog;
        private Double floatingPopPerStore;
        private Double youngPopRatio;
        private Double weekendPopRatio;
        private Double avgOperatingMonths;
        private Double avgClosureMonths;
        private Double opMonthsVsClosureAvg;
        private Double operationMonthsVsSeoulAvg;
        private Double avgSalesPerTransaction;
        private Double weekendSalesRatio;
        private Integer salesMissingFlag;
        private Integer salesMissingTypeCode;
        private Double closureRateLag1;
        private Double closureRateChange;
        private Double totalStoreCountChange;
        private Double floatingPopChange;
        private Double openingRateLag1;
        private Double avgSalesPerTransactionLag1;
    }

    /**
     * FastAPI 칼럼 매핑
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RiskApiRequest {
        @JsonProperty("기준_년분기_코드")
        private Integer baseYearQuarterCode;
        @JsonProperty("기준_분기_코드")
        private Integer quarterCode;
        @JsonProperty("행정동_코드")
        private Integer adminDongCode;
        @JsonProperty("서비스_업종_코드")
        private String serviceIndustryCode;
        @JsonProperty("상권_변화_지표")
        private String commercialChangeCode;
        @JsonProperty("점포_수")
        private Integer operatingStoreCount;
        @JsonProperty("유사_업종_점포_수")
        private Integer totalOperatingStoreCount;
        @JsonProperty("개업_율")
        private Double openingRate;
        @JsonProperty("franchise_ratio")
        private Double operatingFranchiseStoreRatio;
        @JsonProperty("경쟁_밀도")
        private Double competitionDensity;
        @JsonProperty("영역_면적")
        private Double areaSize;
        @JsonProperty("월_평균_소득_금액")
        private Double avgMonthlyIncome;
        @JsonProperty("음식_지출_비율")
        private Double foodExpenditureRatio;
        @JsonProperty("유흥_지출_비율")
        private Double entertainmentExpenditureRatio;
        @JsonProperty("교육_지출_비율")
        private Double educationExpenditureRatio;
        @JsonProperty("여가문화_지출_비율")
        private Double leisureExpenditureRatio;
        @JsonProperty("log_총_유동인구_수")
        private Double floatingPopTotalLog;
        @JsonProperty("floating_population_per_store")
        private Double floatingPopPerStore;
        @JsonProperty("young_pop_ratio")
        private Double youngPopRatio;
        @JsonProperty("weekend_pop_ratio")
        private Double weekendPopRatio;
        @JsonProperty("운영_영업_개월_평균")
        private Double avgOperatingMonths;
        @JsonProperty("폐업_영업_개월_평균")
        private Double avgClosureMonths;
        @JsonProperty("op_months_vs_closure_avg")
        private Double opMonthsVsClosureAvg;
        @JsonProperty("operation_months_vs_seoul_avg")
        private Double operationMonthsVsSeoulAvg;
        @JsonProperty("avg_sales_per_transaction")
        private Double avgSalesPerTransaction;
        @JsonProperty("weekend_sales_ratio")
        private Double weekendSalesRatio;
        @JsonProperty("sales_missing_flag")
        private Integer salesMissingFlag;
        @JsonProperty("sales_missing_type_code")
        private Integer salesMissingTypeCode;
        @JsonProperty("전분기_폐업_률")
        private Double closureRateLag1;
        @JsonProperty("폐업_률_변화_량")
        private Double closureRateChange;
        @JsonProperty("유사_업종_점포_수_변화_량")
        private Double totalStoreCountChange;
        @JsonProperty("유동인구_변화_량")
        private Double floatingPopChange;
        @JsonProperty("개업_율_lag1")
        private Double openingRateLag1;
        @JsonProperty("avg_sales_per_transaction_lag1")
        private Double avgSalesPerTransactionLag1;

        public static RiskApiRequest from(RiskInput input) {
            return RiskApiRequest.builder()
                .baseYearQuarterCode(input.getBaseYearQuarterCode())
                .quarterCode(input.getQuarterCode())
                .adminDongCode(input.getAdminDongCode())
                .serviceIndustryCode(input.getServiceIndustryCode())
                .commercialChangeCode(input.getCommercialChangeCode())
                .operatingStoreCount(input.getOperatingStoreCount())
                .totalOperatingStoreCount(input.getTotalOperatingStoreCount())
                .openingRate(input.getOpeningRate())
                .operatingFranchiseStoreRatio(input.getOperatingFranchiseStoreRatio())
                .competitionDensity(input.getCompetitionDensity())
                .areaSize(input.getAreaSize())
                .avgMonthlyIncome(input.getAvgMonthlyIncome())
                .foodExpenditureRatio(input.getFoodExpenditureRatio())
                .entertainmentExpenditureRatio(input.getEntertainmentExpenditureRatio())
                .educationExpenditureRatio(input.getEducationExpenditureRatio())
                .leisureExpenditureRatio(input.getLeisureExpenditureRatio())
                .floatingPopTotalLog(input.getFloatingPopTotalLog())
                .floatingPopPerStore(input.getFloatingPopPerStore())
                .youngPopRatio(input.getYoungPopRatio())
                .weekendPopRatio(input.getWeekendPopRatio())
                .avgOperatingMonths(input.getAvgOperatingMonths())
                .avgClosureMonths(input.getAvgClosureMonths())
                .opMonthsVsClosureAvg(input.getOpMonthsVsClosureAvg())
                .operationMonthsVsSeoulAvg(input.getOperationMonthsVsSeoulAvg())
                .avgSalesPerTransaction(input.getAvgSalesPerTransaction())
                .weekendSalesRatio(input.getWeekendSalesRatio())
                .salesMissingFlag(input.getSalesMissingFlag())
                .salesMissingTypeCode(input.getSalesMissingTypeCode())
                .closureRateLag1(input.getClosureRateLag1())
                .closureRateChange(input.getClosureRateChange())
                .totalStoreCountChange(input.getTotalStoreCountChange())
                .floatingPopChange(input.getFloatingPopChange())
                .openingRateLag1(input.getOpeningRateLag1())
                .avgSalesPerTransactionLag1(input.getAvgSalesPerTransactionLag1())
                .build();
        }
    }

    /**
     * FastAPI 응답 매핑
     */
    @Getter @Setter @Builder
    @AllArgsConstructor @NoArgsConstructor @ToString
    public static class RiskPredApiResponse {
        @JsonProperty("risk_prob")
        private Double riskProb;

        @JsonProperty("risk_closure_rate")
        private Double riskClosureRate;

        @JsonProperty("risk_level")
        private String riskLevel;

        @JsonProperty("top_risk_factors")
        private List<TopRiskFactor> topRiskFactors;

        private String message;
    }

    /**
     * FastAPI 응답 매핑-중요 피처 목록 매핑
     */
    @Getter @Setter @Builder
    @AllArgsConstructor @NoArgsConstructor @ToString
    public static class TopRiskFactor {
        private String feature;

        @JsonProperty("feature_value")
        private Object featureValue;

        private Double impact;
        private String direction;
    }

    /**
     * 폐업률 output
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RiskOutput {
        private Integer baseYearQuarterCode;
        private Integer adminDongCode;
        private String serviceIndustryCode;
        private Double riskProb;
        private Double riskClosureRate;
        private String riskLevel;
        private Double finalClosureRate;
        private String message;
        private String riskAiResponse;

        private String top1FeatureName;
        private String top1FeatureValue;
        private Double top1Impact;
        private String top1Direction;

        private String top2FeatureName;
        private String top2FeatureValue;
        private Double top2Impact;
        private String top2Direction;

        private String top3FeatureName;
        private String top3FeatureValue;
        private Double top3Impact;
        private String top3Direction;

        private String modelVersion;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
