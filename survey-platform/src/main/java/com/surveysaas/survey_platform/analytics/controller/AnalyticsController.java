package com.surveysaas.survey_platform.analytics.controller;

import com.surveysaas.survey_platform.analytics.dto.SurveyResultsDto;
import com.surveysaas.survey_platform.analytics.service.AnalyticsService;
import com.surveysaas.survey_platform.analytics.sse.SseEmitterRegistry;
import com.surveysaas.survey_platform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surveys/{surveyId}/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final SseEmitterRegistry sseEmitterRegistry;

    // Snapshot de resultados actuales
    @GetMapping
    public ResponseEntity<ApiResponse<SurveyResultsDto>> getResults(
            @PathVariable UUID surveyId) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getResults(surveyId)));
    }

    // Stream de tiempo real — el cliente se suscribe aquí
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable UUID surveyId) {
        return sseEmitterRegistry.register(surveyId);
    }
}