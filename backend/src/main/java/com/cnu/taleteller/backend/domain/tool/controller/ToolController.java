package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import com.cnu.taleteller.backend.domain.tool.domain.UploadFile;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileRequestDto;
import com.cnu.taleteller.backend.domain.tool.service.UploadFileService;
import com.cnu.taleteller.backend.domain.tool.service.S3Service;
import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ScenarioService scenarioService;

    private final UploadFileService uploadFileService;
    private final S3Service s3Service;
    private final ToolService toolService;

    @GetMapping("/s3/image")
    public Map<String, Serializable> s3saveImage(@RequestParam("fileName") String fileName){
        return s3Service.getPreSignedUrl(fileName);
    }

    @PostMapping("/scenario/{bookId}")
    public ResponseEntity saveScenario(@RequestBody String[] scenario, @PathVariable Long bookId){
        List<Scenario> list = scenarioService.save(scenario, bookId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/uploadFile/{bookId}")
    public ResponseEntity saveUploadFile(@RequestBody UploadFileRequestDto dto, @PathVariable Long bookId){
        List<UploadFile> list = uploadFileService.save(dto, bookId);
        System.out.print(list.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

}
