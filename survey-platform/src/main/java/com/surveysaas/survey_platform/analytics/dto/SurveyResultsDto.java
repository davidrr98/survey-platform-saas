package com.surveysaas.survey_platform.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SurveyResultsDto {
    private UUID surveyId;
    private String title;
    private long totalResponses;
    private List<QuestionResultDto> questionResults;
}