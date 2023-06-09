package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileRequestDto;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.tool.service.S3Service;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import com.cnu.taleteller.backend.domain.tool.service.UploadFileService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tool")
@RequiredArgsConstructor
public class ToolController {

    private final UploadFileService uploadFileService;

    private final ToolService toolService;

    @PostMapping("/uploadFile/{bookId}")
    public ResponseEntity saveUploadFile(@RequestBody UploadFileRequestDto dto, @PathVariable Long bookId){
        List<UploadFile> list = uploadFileService.save(dto, bookId);
        System.out.print(list.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/firstAccess/{bookId}")
    public BookData firstAccess(@PathVariable Long bookId) {
        System.out.println(bookId);
        return toolService.firstAccessData(bookId);
    }

    @PostMapping("/scenario/{bookId}")
    public ResponseEntity saveScenario(@RequestBody String scenario, @PathVariable Long bookId){
        String decodedData = null;
        try {
            decodedData = URLDecoder.decode(scenario, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        toolService.saveScenario(decodedData, bookId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
