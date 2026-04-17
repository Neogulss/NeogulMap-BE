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
public class SalesAiResponseDTO {

    private String text;
    private String model;
    private String promptType;
    private String promptVersion;
    private String generatedAt;
}
