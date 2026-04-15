package com.neogulss.neogulmap.report.mapper;

import com.neogulss.neogulmap.report.dto.RiskDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RiskMapper {
    /**
     * 폐업률 예측을 위한 db 조회
     * @param request RiskDTO.RiskUserRequest
     * @return RiskDTO.RiskInput
     */
    RiskDTO.RiskInput selectRiskInput(
      RiskDTO.RiskUserRequest request
    );

    /**
     * 폐업륲 예측 결과 db 저장 및 갱신
     * @param riskOutput RiskDTO.RiskOutput
     * @return int
     */
    int upsertRiskOutput(RiskDTO.RiskOutput riskOutput);

    /**
     * 폐업률 예측 결과 db 조회
     * @param riskOutput RiskDTO.RiskOutput
     * @return
     */
    RiskDTO.RiskOutput selectRiskOutput(
       RiskDTO.RiskOutput riskOutput
    );
}
