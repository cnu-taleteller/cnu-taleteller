package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.dto.CaptionRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.KeywordRequestDto;
import com.cnu.taleteller.backend.domain.tool.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tool/scenario")
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @PostMapping("/")
    public ResponseEntity generateScenario(@RequestBody KeywordRequestDto requestDto){
        try {
            String result = openAiService.generateScenario(requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/flow")
    public ResponseEntity checkScenarioFlow(@RequestBody CaptionRequestDto requestDto){
        try {
            String result = openAiService.generateFlow(requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
