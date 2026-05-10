package com.surveysaas.survey_platform.responses.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ResponseResponseDto {
    private UUID id;
    private UUID respondentToken;
    private UUID surveyId;
    private Instant submittedAt;
    private List<AnswerResponseDto> answers;
}