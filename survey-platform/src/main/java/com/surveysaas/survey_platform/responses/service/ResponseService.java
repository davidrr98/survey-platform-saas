package com.surveysaas.survey_platform.responses.service;

import com.surveysaas.survey_platform.responses.dto.PublicSurveyDto;
import com.surveysaas.survey_platform.responses.dto.PublicSurveyListDto;
import com.surveysaas.survey_platform.responses.dto.ResponseRequestDto;
import com.surveysaas.survey_platform.responses.dto.ResponseResponseDto;

import java.util.List;
import java.util.UUID;

public interface ResponseService {
    PublicSurveyDto getPublicSurvey(UUID surveyId);
    ResponseResponseDto submit(UUID surveyId, ResponseRequestDto request);
    List<PublicSurveyListDto> getAvailableSurveys();
}