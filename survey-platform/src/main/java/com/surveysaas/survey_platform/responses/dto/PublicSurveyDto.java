package com.surveysaas.survey_platform.responses.dto;

import com.surveysaas.survey_platform.questions.dto.QuestionResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PublicSurveyDto {
    private UUID id;
    private String title;
    private String description;
    private List<QuestionResponseDto> questions;
}