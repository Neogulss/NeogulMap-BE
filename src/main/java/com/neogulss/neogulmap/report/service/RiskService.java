package com.neogulss.neogulmap.report.service;

import com.neogulss.neogulmap.report.dto.RiskDTO;
import com.neogulss.neogulmap.report.mapper.RiskMapper;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
public class RiskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonPredRiskUrl;
    private final RiskMapper riskMapper;

    public RiskService(
        @Value("${python.pred.risk-url:${python.ai.risk-url:http://localhost:8002}}")
        String pythonPredRiskUrl,
        RiskMapper riskMapper
    ) {
        this.pythonPredRiskUrl = pythonPredRiskUrl;
        this.riskMapper = riskMapper;
    }

    /**
     * 폐업 위험 예측
     * @param request RiskDTO.RiskUserRequest
     * @return RiskDTO.RiskOutput
     */
    public RiskDTO.RiskOutput predictRisk(RiskDTO.RiskUserRequest request) {
        log.info("[{}-{}] 폐업 위험 예측 시작",
            request.getAdminDongCode(), request.getServiceIndustryCode());

        RiskDTO.RiskInput input = riskMapper.selectRiskInput(request);
        if (input == null) {
            throw new IllegalStateException("예측에 필요한 AI 입력 데이터가 없습니다.");
        }

        RiskDTO.RiskPredApiResponse predResponse = callRiskPrediction(RiskDTO.RiskApiRequest.from(input));
        if (predResponse == null) {
            throw new IllegalStateException("예측 서버 응답이 비어 있습니다.");
        }

        RiskDTO.TopRiskFactor f1 = getFactor(predResponse.getTopRiskFactors(), 0);
        RiskDTO.TopRiskFactor f2 = getFactor(predResponse.getTopRiskFactors(), 1);
        RiskDTO.TopRiskFactor f3 = getFactor(predResponse.getTopRiskFactors(), 2);

        RiskDTO.RiskOutput riskOutput = RiskDTO.RiskOutput.builder()
            .baseYearQuarterCode(input.getBaseYearQuarterCode())
            .adminDongCode(input.getAdminDongCode())
            .serviceIndustryCode(input.getServiceIndustryCode())
            .riskProb(predResponse.getRiskProb())
            .riskClosureRate(predResponse.getRiskClosureRate())
            .riskLevel(predResponse.getRiskLevel())
            .message(predResponse.getMessage())
            .top1FeatureName(f1 != null ? f1.getFeature() : null)
            .top1FeatureValue(toNullableString(f1 != null ? f1.getFeatureValue() : null))
            .top1Impact(f1 != null ? f1.getImpact() : null)
            .top1Direction(f1 != null ? f1.getDirection() : null)
            .top2FeatureName(f2 != null ? f2.getFeature() : null)
            .top2FeatureValue(toNullableString(f2 != null ? f2.getFeatureValue() : null))
            .top2Impact(f2 != null ? f2.getImpact() : null)
            .top2Direction(f2 != null ? f2.getDirection() : null)
            .top3FeatureName(f3 != null ? f3.getFeature() : null)
            .top3FeatureValue(toNullableString(f3 != null ? f3.getFeatureValue() : null))
            .top3Impact(f3 != null ? f3.getImpact() : null)
            .top3Direction(f3 != null ? f3.getDirection() : null)
            .modelVersion("risk-v1")
            .build();

        riskMapper.upsertRiskOutput(riskOutput);

        RiskDTO.RiskOutput saved = riskMapper.selectRiskOutput(riskOutput);
        return saved != null ? saved : riskOutput;
    }

    private RiskDTO.RiskPredApiResponse callRiskPrediction(RiskDTO.RiskApiRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RiskDTO.RiskApiRequest> entity = new HttpEntity<>(request, headers);

        String url = pythonPredRiskUrl + "/report_pred/risk";
        log.info("Call FastAPI risk prediction: {}", url);
        return restTemplate.postForObject(url, entity, RiskDTO.RiskPredApiResponse.class);
    }

    private RiskDTO.TopRiskFactor getFactor(List<RiskDTO.TopRiskFactor> factors, int idx) {
        if (factors == null || factors.size() <= idx) {
            return null;
        }
        return factors.get(idx);
    }

    private String toNullableString(Object value) {
        return value == null ? null : Objects.toString(value, null);
    }
}
