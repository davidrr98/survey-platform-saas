package com.surveysaas.survey_platform.responses.controller;

import com.surveysaas.survey_platform.responses.dto.PublicSurveyDto;
import com.surveysaas.survey_platform.responses.dto.PublicSurveyListDto;
import com.surveysaas.survey_platform.responses.dto.ResponseRequestDto;
import com.surveysaas.survey_platform.responses.dto.ResponseResponseDto;
import com.surveysaas.survey_platform.responses.service.ResponseService;
import com.surveysaas.survey_platform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/surveys")
@RequiredArgsConstructor
public class PublicSurveyController {

    private final ResponseService responseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublicSurveyListDto>>> getAvailableSurveys() {
        return ResponseEntity.ok(ApiResponse.success(responseService.getAvailableSurveys()));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<PublicSurveyDto>> getSurvey(
            @PathVariable UUID surveyId) {
        return ResponseEntity.ok(ApiResponse.success(responseService.getPublicSurvey(surveyId)));
    }

    @PostMapping("/{surveyId}/respond")
    public ResponseEntity<ApiResponse<ResponseResponseDto>> submit(
            @PathVariable UUID surveyId,
            @Valid @RequestBody ResponseRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseService.submit(surveyId, request)));
    }
}