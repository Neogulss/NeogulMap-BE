package com.neogulss.neogulmap.report.mapper;

import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesPredMapper {

    /**
     * 매출 예측 입력값 조회
     * REPORT_DATA_COMMON + REPORT_DATA_SALES JOIN
     */
    SalesPredDTO.SalesInput selectSalesPredInput(SalesPredDTO.SalesUserRequest request);

    /**
     * 저장된 매출 예측 결과 조회 (캐싱용)
     */
    SalesPredDTO.SalesOutput selectSalesPredResult(SalesPredDTO.SalesOutput salesOutput);

    /**
     * 매출 예측 결과 저장/갱신
     * 성공 시 1, 실패 시 0 반환
     */
    int upsertSalesPredResult(SalesPredDTO.SalesOutput salesOutput);
}
