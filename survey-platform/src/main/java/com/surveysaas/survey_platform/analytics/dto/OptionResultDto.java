package com.surveysaas.survey_platform.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OptionResultDto {
    private UUID optionId;
    private String text;
    private long count;
    private double percentage;
}