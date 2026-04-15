package com.neogulss.neogulmap.report.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.report.dto.ReportDTO;
import com.neogulss.neogulmap.report.service.SalesPredService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report/pred")
@RequiredArgsConstructor
public class SalesPredController {

    private final SalesPredService salesPredService;

    /**
     * 월 매출 예측 조회
     *
     * @param request ReportDTO.Request (adminDongCode, serviceIndustryCode)
     * @return ResponseEntity.ok(response)
     */
    @PostMapping("/sales")
    public ResponseEntity<BaseResponse<Object>> getSalesPredReport(
            @RequestBody ReportDTO.Request request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(salesPredService.getSalesPredReport(request))
                .build();
        return ResponseEntity.ok(response);
    }
}