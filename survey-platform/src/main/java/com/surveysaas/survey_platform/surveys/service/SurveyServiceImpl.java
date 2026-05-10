package com.surveysaas.survey_platform.surveys.service;

import com.surveysaas.survey_platform.shared.exception.ResourceNotFoundException;
import com.surveysaas.survey_platform.surveys.domain.Survey;
import com.surveysaas.survey_platform.surveys.dto.SurveyRequestDto;
import com.surveysaas.survey_platform.surveys.dto.SurveyResponseDto;
import com.surveysaas.survey_platform.surveys.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    @Override
    @Transactional
    public SurveyResponseDto create(SurveyRequestDto request) {
        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        return toDto(surveyRepository.save(survey));
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyResponseDto findById(UUID id) {
        return surveyRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Survey no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyResponseDto> findAll() {
        return surveyRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public SurveyResponseDto update(UUID id, SurveyRequestDto request) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey no encontrado con id: " + id));
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setStatus(request.getStatus());
        return toDto(surveyRepository.save(survey));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!surveyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Survey no encontrado con id: " + id);
        }
        surveyRepository.deleteById(id);
    }

    private SurveyResponseDto toDto(Survey survey) {
        return SurveyResponseDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .status(survey.getStatus())
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .build();
    }
}