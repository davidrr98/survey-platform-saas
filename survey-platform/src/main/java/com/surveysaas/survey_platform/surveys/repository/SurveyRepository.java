package com.surveysaas.survey_platform.surveys.repository;

import com.surveysaas.survey_platform.surveys.domain.Survey;
import com.surveysaas.survey_platform.surveys.domain.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
    List<Survey> findByStatus(SurveyStatus status);
}
