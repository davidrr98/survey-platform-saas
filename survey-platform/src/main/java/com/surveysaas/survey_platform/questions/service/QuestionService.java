package com.surveysaas.survey_platform.questions.service;

import com.surveysaas.survey_platform.questions.dto.QuestionRequestDto;
import com.surveysaas.survey_platform.questions.dto.QuestionResponseDto;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    QuestionResponseDto create(UUID surveyId, QuestionRequestDto request);
    QuestionResponseDto findById(UUID id);
    List<QuestionResponseDto> findBySurveyId(UUID surveyId);
    QuestionResponseDto update(UUID id, QuestionRequestDto request);
    void delete(UUID id);
}