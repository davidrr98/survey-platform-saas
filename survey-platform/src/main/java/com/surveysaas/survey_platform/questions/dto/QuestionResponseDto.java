package com.surveysaas.survey_platform.questions.dto;

import com.surveysaas.survey_platform.questions.domain.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class QuestionResponseDto {
    private UUID id;
    private String text;
    private QuestionType type;
    private Integer questionOrder;
    private Boolean required;
    private List<OptionResponseDto> options;
}