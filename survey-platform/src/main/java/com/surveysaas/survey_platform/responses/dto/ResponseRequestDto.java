package com.surveysaas.survey_platform.responses.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ResponseRequestDto {

    @NotEmpty(message = "Debe incluir al menos una respuesta")
    private List<AnswerRequestDto> answers;
}