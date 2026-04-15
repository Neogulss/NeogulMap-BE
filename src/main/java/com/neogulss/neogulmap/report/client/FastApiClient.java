package com.neogulss.neogulmap.report.client;

import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fastApiClient", url = "${python.ai.pred-url}")
public interface FastApiClient {

    @PostMapping("/report_pred/sales")
    SalesPredDTO.Response predictSales(@RequestBody SalesPredDTO.Request request);
}