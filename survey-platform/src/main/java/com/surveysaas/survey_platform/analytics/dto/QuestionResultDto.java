package com.surveysaas.survey_platform.analytics.dto;

import com.surveysaas.survey_platform.questions.domain.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class QuestionResultDto {
    private UUID questionId;
    private String text;
    private QuestionType type;
    private Double average;
    private List<OptionResultDto> optionResults;
}