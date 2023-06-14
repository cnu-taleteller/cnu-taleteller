package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.dto.CaptionRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.KeywordRequestDto;
import com.cnu.taleteller.backend.domain.tool.service.ApiService;
import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/v1/tool/scenario")
@RequiredArgsConstructor
public class ScenarioController {

    private final ApiService apiService;
    private final ScenarioService scenarioService;

    @PostMapping("/")
    public DeferredResult<ResponseEntity> generateScenario(@RequestBody KeywordRequestDto requestDto) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>(120000L); // 120초(2분) 타임아웃 설정

        apiService.enqueueRequest(() -> {
            try {
                System.out.println("실행중");
                String result = scenarioService.generateScenario(requestDto);
                deferredResult.setResult(ResponseEntity.ok(result));
            } catch (Exception e) {
                deferredResult.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            }
        });

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
        });

        return deferredResult;
    }

    @PostMapping("/flow")
    public DeferredResult<ResponseEntity> checkScenarioFlow(@RequestBody CaptionRequestDto requestDto) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>(60000L); // 60초(1분) 타임아웃 설정

        apiService.enqueueRequest(() -> {
            try {
                System.out.println("실행중");
                String result = scenarioService.generateFlow(requestDto);
                deferredResult.setResult(ResponseEntity.ok(result));
            } catch (Exception e) {
                deferredResult.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            }
        });

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
        });

        return deferredResult;
    }
}

