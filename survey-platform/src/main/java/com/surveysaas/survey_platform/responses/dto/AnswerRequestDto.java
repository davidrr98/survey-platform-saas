package com.surveysaas.survey_platform.responses.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AnswerRequestDto {

    @NotNull(message = "El id de la pregunta es obligatorio")
    private UUID questionId;

    // Opcional: para SINGLE_CHOICE, MULTIPLE_CHOICE
    private UUID optionId;

    // Opcional: para OPEN_TEXT, RATING
    private String textValue;
}