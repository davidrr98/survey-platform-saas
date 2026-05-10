package com.surveysaas.survey_platform.surveys.dto;

import com.surveysaas.survey_platform.surveys.domain.SurveyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SurveyRequestDto {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar 200 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String description;

    @NotNull(message = "El estado es obligatorio")
    private SurveyStatus status;
}