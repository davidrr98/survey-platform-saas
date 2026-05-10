package com.surveysaas.survey_platform.questions.repository;

import com.surveysaas.survey_platform.questions.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findBySurveyIdOrderByQuestionOrderAsc(UUID surveyId);
    void deleteBySurveyId(UUID surveyId);
    long countBySurveyId(UUID surveyId);
}