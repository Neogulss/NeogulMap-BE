package com.neogulss.neogulmap.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesAiPromptDTO {

    private Integer baseYearQuarterCode;
    private Integer predYearQuarterCode;
    private Integer adminDongCode;
    private String serviceIndustryCode;
    private Long predSalesPerStore;
    private String confidence;
    private Integer segment;
    private Integer operatingStoreCount;
    private Float competitionDensity;
    private Integer avgMonthlyIncome;
    private String topSalesFactors;
    private String message;

    public static SalesAiPromptDTO from(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput
    ) {
        return SalesAiPromptDTO.builder()
                .baseYearQuarterCode(salesInput != null ? salesInput.getBaseYearQuarterCode() : null)
                .predYearQuarterCode(salesOutput != null ? salesOutput.getPredYearQuarterCode() : null)
                .adminDongCode(salesOutput != null ? salesOutput.getAdminDongCode() : null)
                .serviceIndustryCode(salesOutput != null ? salesOutput.getServiceIndustryCode() : null)
                .predSalesPerStore(salesOutput != null ? salesOutput.getPredSalesPerStore() : null)
                .confidence(salesOutput != null ? salesOutput.getConfidence() : null)
                .segment(salesOutput != null ? salesOutput.getSegment() : null)
                .operatingStoreCount(salesInput != null ? salesInput.getOperatingStoreCount() : null)
                .competitionDensity(salesInput != null ? salesInput.getCompetitionDensity() : null)
                .avgMonthlyIncome(salesInput != null ? salesInput.getAvgMonthlyIncome() : null)
                .topSalesFactors(salesOutput != null ? salesOutput.getTopSalesFactors() : null)
                .message(salesOutput != null ? salesOutput.getMessage() : null)
                .build();
    }
}
