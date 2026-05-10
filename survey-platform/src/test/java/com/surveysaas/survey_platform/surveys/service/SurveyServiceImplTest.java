package com.surveysaas.survey_platform.surveys.service;

import com.surveysaas.survey_platform.shared.exception.ResourceNotFoundException;
import com.surveysaas.survey_platform.surveys.domain.Survey;
import com.surveysaas.survey_platform.surveys.domain.SurveyStatus;
import com.surveysaas.survey_platform.surveys.dto.SurveyRequestDto;
import com.surveysaas.survey_platform.surveys.dto.SurveyResponseDto;
import com.surveysaas.survey_platform.surveys.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SurveyServiceImpl")
class SurveyServiceImplTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyServiceImpl surveyService;

    private Survey surveyMock;
    private UUID surveyId;

    @BeforeEach
    void setUp() {
        surveyId = UUID.randomUUID();
        surveyMock = Survey.builder()
                .id(surveyId)
                .title("Encuesta de prueba")
                .description("Descripción de prueba")
                .status(SurveyStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("create — debe persistir y retornar el survey correctamente")
    void create_shouldPersistAndReturnSurvey() {
        // Arrange
        SurveyRequestDto request = new SurveyRequestDto();
        request.setTitle("Encuesta de prueba");
        request.setDescription("Descripción de prueba");
        request.setStatus(SurveyStatus.ACTIVE);

        when(surveyRepository.save(any(Survey.class))).thenReturn(surveyMock);

        // Act
        SurveyResponseDto result = surveyService.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(surveyId);
        assertThat(result.getTitle()).isEqualTo("Encuesta de prueba");
        assertThat(result.getStatus()).isEqualTo(SurveyStatus.ACTIVE);
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    @DisplayName("findById — debe lanzar ResourceNotFoundException cuando el survey no existe")
    void findById_shouldThrowResourceNotFoundException_whenSurveyNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(surveyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> surveyService.findById(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(nonExistentId.toString());

        verify(surveyRepository, times(1)).findById(nonExistentId);
    }
}