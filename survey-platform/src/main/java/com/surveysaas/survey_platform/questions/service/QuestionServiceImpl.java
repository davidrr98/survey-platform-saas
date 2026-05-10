package com.surveysaas.survey_platform.questions.service;

import com.surveysaas.survey_platform.questions.domain.Question;
import com.surveysaas.survey_platform.questions.domain.Option;
import com.surveysaas.survey_platform.questions.dto.OptionResponseDto;
import com.surveysaas.survey_platform.questions.dto.QuestionRequestDto;
import com.surveysaas.survey_platform.questions.dto.QuestionResponseDto;
import com.surveysaas.survey_platform.questions.repository.QuestionRepository;
import com.surveysaas.survey_platform.shared.exception.ResourceNotFoundException;
import com.surveysaas.survey_platform.surveys.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    @Override
    @Transactional
    public QuestionResponseDto create(UUID surveyId, QuestionRequestDto request) {
        var survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey no encontrado con id: " + surveyId));

        var question = Question.builder()
                .text(request.getText())
                .type(request.getType())
                .questionOrder(request.getQuestionOrder())
                .required(request.getRequired())
                .survey(survey)
                .build();

        // Agrega las opciones si el tipo las requiere
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            var options = request.getOptions().stream()
                    .map(optDto -> Option.builder()
                            .text(optDto.getText())
                            .optionOrder(optDto.getOptionOrder())
                            .question(question)
                            .build())
                    .toList();
            question.getOptions().addAll(options);
        }

        return toDto(questionRepository.save(question));
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDto findById(UUID id) {
        return questionRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponseDto> findBySurveyId(UUID surveyId) {
        if (!surveyRepository.existsById(surveyId)) {
            throw new ResourceNotFoundException("Survey no encontrado con id: " + surveyId);
        }
        return questionRepository.findBySurveyIdOrderByQuestionOrderAsc(surveyId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public QuestionResponseDto update(UUID id, QuestionRequestDto request) {
        var question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));

        question.setText(request.getText());
        question.setType(request.getType());
        question.setQuestionOrder(request.getQuestionOrder());
        question.setRequired(request.getRequired());

        // Reemplaza opciones completamente
        question.getOptions().clear();
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            var options = request.getOptions().stream()
                    .map(optDto -> Option.builder()
                            .text(optDto.getText())
                            .optionOrder(optDto.getOptionOrder())
                            .question(question)
                            .build())
                    .toList();
            question.getOptions().addAll(options);
        }

        return toDto(questionRepository.save(question));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pregunta no encontrada con id: " + id);
        }
        questionRepository.deleteById(id);
    }

    // ── Mapper ──────────────────────────────────────────────────────────────

    private QuestionResponseDto toDto(Question question) {
        return QuestionResponseDto.builder()
                .id(question.getId())
                .text(question.getText())
                .type(question.getType())
                .questionOrder(question.getQuestionOrder())
                .required(question.getRequired())
                .options(question.getOptions().stream()
                        .map(opt -> OptionResponseDto.builder()
                                .id(opt.getId())
                                .text(opt.getText())
                                .optionOrder(opt.getOptionOrder())
                                .build())
                        .toList())
                .build();
    }
}