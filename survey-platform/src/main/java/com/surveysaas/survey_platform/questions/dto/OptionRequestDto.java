package com.surveysaas.survey_platform.questions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OptionRequestDto {

    @NotBlank(message = "El texto de la opción es obligatorio")
    private String text;

    @NotNull(message = "El orden es obligatorio")
    @Positive(message = "El orden debe ser mayor a 0")
    private Integer optionOrder;
}