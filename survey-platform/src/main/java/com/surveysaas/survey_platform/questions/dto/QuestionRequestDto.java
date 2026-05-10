package com.surveysaas.survey_platform.questions.dto;

import com.surveysaas.survey_platform.questions.domain.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionRequestDto {

    @NotBlank(message = "El texto de la pregunta es obligatorio")
    private String text;

    @NotNull(message = "El tipo de pregunta es obligatorio")
    private QuestionType type;

    @NotNull(message = "El orden es obligatorio")
    @Positive(message = "El orden debe ser mayor a 0")
    private Integer questionOrder;

    @NotNull(message = "Debe indicar si la pregunta es obligatoria")
    private Boolean required;

    @Valid
    private List<OptionRequestDto> options = new ArrayList<>();
}