package com.surveysaas.survey_platform.surveys.controller;

import com.surveysaas.survey_platform.shared.response.ApiResponse;
import com.surveysaas.survey_platform.surveys.dto.SurveyRequestDto;
import com.surveysaas.survey_platform.surveys.dto.SurveyResponseDto;
import com.surveysaas.survey_platform.surveys.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<ApiResponse<SurveyResponseDto>> create(
            @Valid @RequestBody SurveyRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(surveyService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(surveyService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SurveyResponseDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(surveyService.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody SurveyRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(surveyService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        surveyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

