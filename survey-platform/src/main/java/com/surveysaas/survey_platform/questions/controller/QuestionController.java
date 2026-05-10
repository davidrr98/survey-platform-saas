package com.surveysaas.survey_platform.questions.controller;

import com.surveysaas.survey_platform.questions.dto.QuestionRequestDto;
import com.surveysaas.survey_platform.questions.dto.QuestionResponseDto;
import com.surveysaas.survey_platform.questions.service.QuestionService;
import com.surveysaas.survey_platform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surveys/{surveyId}/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponseDto>> create(
            @PathVariable UUID surveyId,
            @Valid @RequestBody QuestionRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(questionService.create(surveyId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> findBySurvey(
            @PathVariable UUID surveyId) {
        return ResponseEntity.ok(ApiResponse.success(questionService.findBySurveyId(surveyId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> findById(
            @PathVariable UUID surveyId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(questionService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> update(
            @PathVariable UUID surveyId,
            @PathVariable UUID id,
            @Valid @RequestBody QuestionRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(questionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID surveyId,
            @PathVariable UUID id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}