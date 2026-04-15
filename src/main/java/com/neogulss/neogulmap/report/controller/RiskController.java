package com.neogulss.neogulmap.report.controller;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.report.dto.RiskDTO;
import com.neogulss.neogulmap.report.service.RiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pred")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;

    /**
     * 폐업 위험 예측
     *
     * @param request RiskDTO.RiskUserRequest
     * @return ResponseEntity.ok(response)
     */
    @PostMapping("/risk")
    public ResponseEntity<BaseResponse<Object>> predictRisk(
            @RequestBody RiskDTO.RiskUserRequest request) {
        BaseResponse<Object> response = BaseResponse.builder()
                .data(riskService.predictRisk(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
