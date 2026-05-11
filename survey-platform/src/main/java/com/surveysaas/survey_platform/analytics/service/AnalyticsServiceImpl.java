package com.surveysaas.survey_platform.analytics.service;

import com.surveysaas.survey_platform.analytics.dto.OptionResultDto;
import com.surveysaas.survey_platform.analytics.dto.QuestionResultDto;
import com.surveysaas.survey_platform.analytics.dto.SurveyResultsDto;
import com.surveysaas.survey_platform.questions.repository.QuestionRepository;
import com.surveysaas.survey_platform.responses.domain.Answer;
import com.surveysaas.survey_platform.responses.repository.ResponseRepository;
import com.surveysaas.survey_platform.shared.exception.ResourceNotFoundException;
import com.surveysaas.survey_platform.surveys.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;

    @Override
    @Transactional(readOnly = true)
    public SurveyResultsDto getResults(UUID surveyId) {
        var survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada con id: " + surveyId));

        long totalResponses = responseRepository.countBySurveyId(surveyId);

        List<QuestionResultDto> questionResults = questionRepository
                .findBySurveyIdOrderByQuestionOrderAsc(surveyId)
                .stream()
                .map(question -> buildQuestionResult(question, totalResponses, surveyId))
                .toList();

        return SurveyResultsDto.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .totalResponses(totalResponses)
                .questionResults(questionResults)
                .build();
    }

    // ── Builders privados ────────────────────────────────────────────────────

    private QuestionResultDto buildQuestionResult(
            com.surveysaas.survey_platform.questions.domain.Question question,
            long totalResponses,
            UUID surveyId) {

        Double average = null;
        List<OptionResultDto> optionResults = List.of();

        switch (question.getType()) {
            case NUMERIC, RATING -> average = calculateAverage(question.getAnswers());
            case SINGLE_CHOICE, MULTIPLE_CHOICE -> optionResults = buildOptionResults(question, totalResponses, surveyId);
            case YES_NO -> optionResults = buildYesNoResults(question.getAnswers(), totalResponses);
            case TEXT -> { /* sin promedio ni opciones */ }
        }

        return QuestionResultDto.builder()
                .questionId(question.getId())
                .text(question.getText())
                .type(question.getType())
                .average(average)
                .optionResults(optionResults)
                .build();
    }

    private List<OptionResultDto> buildOptionResults(
            com.surveysaas.survey_platform.questions.domain.Question question,
            long totalResponses,
            UUID surveyId) {

        return question.getOptions().stream()
                .map(option -> {
                    long count = option.getAnswers() == null ? 0 :
                            option.getAnswers().stream()
                            .filter(a -> a.getResponse().getSurvey().getId().equals(surveyId))
                            .count();

                    double percentage = totalResponses > 0
                            ? Math.round((count * 100.0 / totalResponses) * 10.0) / 10.0
                            : 0.0;

                    return OptionResultDto.builder()
                            .optionId(option.getId())
                            .text(option.getText())
                            .count(count)
                            .percentage(percentage)
                            .build();
                })
                .toList();
    }

    private List<OptionResultDto> buildYesNoResults(List<Answer> answers, long totalResponses) {
        long yesCount = answers.stream()
                .map(Answer::getTextValue)
                .filter(v -> v != null && (v.equalsIgnoreCase("true")
                        || v.equalsIgnoreCase("si")
                        || v.equalsIgnoreCase("sí")
                        || v.equalsIgnoreCase("yes")))
                .count();
        long noCount = answers.stream()
                .map(Answer::getTextValue)
                .filter(v -> v != null && (v.equalsIgnoreCase("false")
                        || v.equalsIgnoreCase("no")))
                .count();

        double yesPercentage = totalResponses > 0
                ? Math.round((yesCount * 100.0 / totalResponses) * 10.0) / 10.0 : 0.0;
        double noPercentage = totalResponses > 0
                ? Math.round((noCount * 100.0 / totalResponses) * 10.0) / 10.0 : 0.0;

        return List.of(
                OptionResultDto.builder().optionId(null).text("Sí").count(yesCount).percentage(yesPercentage).build(),
                OptionResultDto.builder().optionId(null).text("No").count(noCount).percentage(noPercentage).build()
        );
    }

    private Double calculateAverage(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) return 0.0;

        double avg = answers.stream()
                .map(Answer::getTextValue)
                .filter(v -> v != null && !v.isBlank())
                .mapToDouble(v -> {
                    try {
                        return Double.parseDouble(v);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);

        return Math.round(avg * 10.0) / 10.0;
    }
}