package com.neogulss.neogulmap.report.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
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
@RequestMapping("/pred")
@RequiredArgsConstructor
public class SalesPredController {

    private final SalesPredService salesPredService;

    /**
     * 월 매출 예측 조회
     *
     * @param request SalesPredDTO.SalesUserRequest (adminDongCode, serviceIndustryCode)
     * @return ResponseEntity.ok(response)
     */
    @PostMapping("/sales")
    public ResponseEntity<BaseResponse<Object>> getSalesPredReport(
            @RequestBody SalesPredDTO.SalesUserRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(salesPredService.getSalesPredReport(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
