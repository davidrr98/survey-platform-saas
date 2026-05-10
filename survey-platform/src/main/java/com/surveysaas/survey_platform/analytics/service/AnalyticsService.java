package com.surveysaas.survey_platform.analytics.service;

import com.surveysaas.survey_platform.analytics.dto.SurveyResultsDto;

import java.util.UUID;

public interface AnalyticsService {
    SurveyResultsDto getResults(UUID surveyId);
}