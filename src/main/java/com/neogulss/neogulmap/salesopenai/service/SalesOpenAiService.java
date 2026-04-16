package com.neogulss.neogulmap.salesopenai.service;

import com.neogulss.neogulmap.salesopenai.dto.OpenAiDTO;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOpenAiService {

    private final OpenAiService openAiService;

    public String generateComment(SalesPredDTO.SalesInput salesInput, SalesPredDTO.SalesOutput salesOutput) {
        String prompt = buildSalesCommentPrompt(salesInput, salesOutput, null);

        try {
            return openAiService.ask(prompt).getText();
        } catch (Exception e) {
            log.warn("[{}] OpenAI 매출 설명 생성 실패 - reason: {}",
                    salesOutput.getAdminDongCode(), e.getMessage());
            return null;
        }
    }

    public OpenAiDTO.PromptTestResponse testSalesComment(OpenAiDTO.SalesCommentTestRequest request) {
        String prompt = buildSalesCommentPrompt(
                request.getSalesInput(),
                request.getSalesOutput(),
                request.getInstructionOverride()
        );

        OpenAiDTO.TextResponse aiResponse = openAiService.ask(prompt);
        return OpenAiDTO.PromptTestResponse.builder()
                .model(aiResponse.getModel())
                .prompt(prompt)
                .text(aiResponse.getText())
                .build();
    }

    private String buildSalesCommentPrompt(
            SalesPredDTO.SalesInput salesInput,
            SalesPredDTO.SalesOutput salesOutput,
            String instructionOverride
    ) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(resolveInstruction(instructionOverride));
        if (!prompt.toString().endsWith("\n\n")) {
            prompt.append("\n\n");
        }
        prompt.append("입력 데이터:\n");
        prompt.append("- 기준 분기 코드: ").append(salesInput.getBaseYearQuarterCode()).append('\n');
        prompt.append("- 예측 분기 코드: ").append(salesOutput.getPredYearQuarterCode()).append('\n');
        prompt.append("- 행정동 코드: ").append(salesOutput.getAdminDongCode()).append('\n');
        prompt.append("- 서비스 업종 코드: ").append(salesOutput.getServiceIndustryCode()).append('\n');
        prompt.append("- 예측 점포당 매출: ").append(salesOutput.getPredSalesPerStore()).append("원\n");
        prompt.append("- 신뢰도: ").append(safeValue(salesOutput.getConfidence())).append('\n');
        prompt.append("- 세그먼트: ").append(safeValue(salesOutput.getSegment())).append('\n');
        prompt.append("- 점포 수: ").append(safeValue(salesInput.getOperatingStoreCount())).append('\n');
        prompt.append("- 경쟁 밀도: ").append(safeValue(salesInput.getCompetitionDensity())).append('\n');
        prompt.append("- 월 평균 소득 금액: ").append(safeValue(salesInput.getAvgMonthlyIncome())).append('\n');
        prompt.append("- 주요 요인(JSON): ").append(safeValue(salesOutput.getTopSalesFactors())).append('\n');
        return prompt.toString();
    }

    private String resolveInstruction(String instructionOverride) {
        if (instructionOverride != null && !instructionOverride.isBlank()) {
            return instructionOverride.trim();
        }

        StringBuilder instruction = new StringBuilder();
        instruction.append("너는 상권 매출 분석가다.\n");
        instruction.append("아래 예측 데이터를 바탕으로 한국어 설명문을 작성해라.\n");
        instruction.append("출력 규칙:\n");
        instruction.append("1. 문단은 2~3문장으로 작성한다.\n");
        instruction.append("2. 문체는 담백한 보고서형 서술문으로 작성한다.\n");
        instruction.append("3. 첫 문장은 전체 전망 요약, 둘째 문장은 데이터 근거, 셋째 문장은 해석 포인트나 유의점을 다룬다.\n");
        instruction.append("4. 과장 표현, 감탄문, 마크다운, 불릿 포인트는 사용하지 않는다.\n");
        instruction.append("5. 제공된 숫자와 정보만 사용하고, 없는 사실은 추정하지 않는다.\n");
        instruction.append("6. 업종과 상권 특성이 자연스럽게 읽히도록 문장을 매끈하게 연결한다.\n");
        instruction.append("7. 응답 본문만 출력하고 제목이나 라벨은 쓰지 않는다.");
        return instruction.toString();
    }

    private String safeValue(Object value) {
        return value == null ? "없음" : String.valueOf(value);
    }
}
