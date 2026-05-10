package com.surveysaas.survey_platform.surveys.dto;

import com.surveysaas.survey_platform.surveys.domain.SurveyStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SurveyResponseDto {
    private UUID id;
    private String title;
    private String description;
    private SurveyStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}