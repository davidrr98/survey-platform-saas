package com.surveysaas.survey_platform.responses.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AnswerResponseDto {
    private UUID questionId;
    private UUID optionId;
    private String textValue;
}