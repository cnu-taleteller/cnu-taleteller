package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.dto.CaptionRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.KeywordRequestDto;
import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/api/v1/tool/scenario")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;

    @PostMapping("/")
    public ResponseEntity generateScenario(@RequestBody KeywordRequestDto requestDto){
        try {
            String result = scenarioService.generateScenario(requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/flow")
    public ResponseEntity checkScenarioFlow(@RequestBody CaptionRequestDto requestDto){
        try {
            String result = scenarioService.generateFlow(requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{bookId}")
    public ResponseEntity saveScenario(@RequestBody String scenario, @PathVariable Long bookId){
        String decodedData = null;
        try {
            decodedData = URLDecoder.decode(scenario, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        scenarioService.save(decodedData, bookId);
        return new ResponseEntity(HttpStatus.OK);
    }



}
