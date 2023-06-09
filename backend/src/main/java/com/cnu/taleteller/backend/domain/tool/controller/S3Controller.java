package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tool/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/image")
    public Map<String, Serializable> s3saveImage(@RequestParam("fileName") String fileName){
        return s3Service.getPreSignedUrl(fileName);
    }

}
