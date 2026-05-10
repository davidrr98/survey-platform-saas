package com.surveysaas.survey_platform.surveys.service;

import com.surveysaas.survey_platform.surveys.dto.SurveyRequestDto;
import com.surveysaas.survey_platform.surveys.dto.SurveyResponseDto;

import java.util.List;
import java.util.UUID;

public interface SurveyService {
    SurveyResponseDto create(SurveyRequestDto request);
    SurveyResponseDto findById(UUID id);
    List<SurveyResponseDto> findAll();
    SurveyResponseDto update(UUID id, SurveyRequestDto request);
    void delete(UUID id);
}
