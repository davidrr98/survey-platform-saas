package com.surveysaas.survey_platform.responses.service;

import com.surveysaas.survey_platform.analytics.dto.SurveyResultsDto;
import com.surveysaas.survey_platform.analytics.service.AnalyticsService;
import com.surveysaas.survey_platform.analytics.sse.SseEmitterRegistry;
import com.surveysaas.survey_platform.questions.domain.Option;
import com.surveysaas.survey_platform.questions.repository.QuestionRepository;
import com.surveysaas.survey_platform.responses.domain.Answer;
import com.surveysaas.survey_platform.responses.domain.Response;
import com.surveysaas.survey_platform.responses.dto.*;
import com.surveysaas.survey_platform.responses.repository.ResponseRepository;
import com.surveysaas.survey_platform.shared.exception.ResourceNotFoundException;
import com.surveysaas.survey_platform.surveys.domain.Survey;
import com.surveysaas.survey_platform.surveys.domain.SurveyStatus;
import com.surveysaas.survey_platform.surveys.repository.SurveyRepository;
import com.surveysaas.survey_platform.questions.dto.OptionResponseDto;
import com.surveysaas.survey_platform.questions.dto.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final ResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final SseEmitterRegistry sseEmitterRegistry;
    private final AnalyticsService analyticsService;

    @Override
    @Transactional(readOnly = true)
    public PublicSurveyDto getPublicSurvey(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada"));

        if (survey.getStatus() != SurveyStatus.ACTIVE) {
            throw new IllegalStateException("La encuesta no está disponible");
        }

        List<QuestionResponseDto> questions = questionRepository
                .findBySurveyIdOrderByQuestionOrderAsc(surveyId)
                .stream()
                .map(q -> QuestionResponseDto.builder()
                        .id(q.getId())
                        .text(q.getText())
                        .type(q.getType())
                        .questionOrder(q.getQuestionOrder())
                        .required(q.getRequired())
                        .options(q.getOptions().stream()
                                .map(opt -> OptionResponseDto.builder()
                                        .id(opt.getId())
                                        .text(opt.getText())
                                        .optionOrder(opt.getOptionOrder())
                                        .build())
                                .toList())
                        .build())
                .toList();

        return PublicSurveyDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .questions(questions)
                .build();
    }

    @Override
    @Transactional
    public ResponseResponseDto submit(UUID surveyId, ResponseRequestDto request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada"));

        if (survey.getStatus() != SurveyStatus.ACTIVE) {
            throw new IllegalStateException("La encuesta no está disponible para responder");
        }

        UUID respondentToken = UUID.randomUUID();

        Response response = Response.builder()
                .survey(survey)
                .respondentToken(respondentToken)
                .build();

        List<Answer> answers = request.getAnswers().stream()
                .map(answerDto -> {
                    var question = questionRepository.findById(answerDto.getQuestionId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Pregunta no encontrada: " + answerDto.getQuestionId()));

                    Option selectedOption = null;
                    if (answerDto.getOptionId() != null) {
                        selectedOption = question.getOptions().stream()
                                .filter(opt -> opt.getId().equals(answerDto.getOptionId()))
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Opción no encontrada: " + answerDto.getOptionId()));
                    }

                    return Answer.builder()
                            .response(response)
                            .question(question)
                            .selectedOption(selectedOption)
                            .textValue(answerDto.getTextValue())
                            .build();
                })
                .toList();

        response.getAnswers().addAll(answers);
        Response saved = responseRepository.save(response);
        SurveyResultsDto updatedResults = analyticsService.getResults(surveyId);
        sseEmitterRegistry.broadcast(surveyId, updatedResults);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicSurveyListDto> getAvailableSurveys() {
        return surveyRepository.findByStatus(SurveyStatus.ACTIVE)
                .stream()
                .map(survey -> PublicSurveyListDto.builder()
                        .id(survey.getId())
                        .title(survey.getTitle())
                        .description(survey.getDescription())
                        .totalQuestions(questionRepository.countBySurveyId(survey.getId()))
                        .build())
                .toList();
    }

    // ── Mapper ──────────────────────────────────────────────────────────────

    private ResponseResponseDto toDto(Response response) {
        return ResponseResponseDto.builder()
                .id(response.getId())
                .respondentToken(response.getRespondentToken())
                .surveyId(response.getSurvey().getId())
                .submittedAt(response.getSubmittedAt())
                .answers(response.getAnswers().stream()
                        .map(a -> AnswerResponseDto.builder()
                                .questionId(a.getQuestion().getId())
                                .optionId(a.getSelectedOption() != null
                                        ? a.getSelectedOption().getId() : null)
                                .textValue(a.getTextValue())
                                .build())
                        .toList())
                .build();
    }
}