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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tool")
@RequiredArgsConstructor
public class ToolController {

    private final UploadFileService uploadFileService;
    private final S3Service s3Service;
    private final ToolService toolService;

    @GetMapping("/s3/image")
    public Map<String, Serializable> s3saveImage(@RequestParam("fileName") String fileName){
        return s3Service.getPreSignedUrl(fileName);
    }

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
}
