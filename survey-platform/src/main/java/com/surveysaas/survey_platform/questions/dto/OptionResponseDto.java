package com.surveysaas.survey_platform.questions.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OptionResponseDto {
    private UUID id;
    private String text;
    private Integer optionOrder;
}