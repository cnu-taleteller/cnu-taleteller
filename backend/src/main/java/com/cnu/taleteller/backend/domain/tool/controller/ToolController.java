package com.cnu.taleteller.backend.domain.tool.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/tool")
public class ToolController {

    // 임시로 만든 Tool Controller 입니다

    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addPhoto(@RequestParam("image") MultipartFile img) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + img.getOriginalFilename();
        String path = "D:/project/cnu-taleteller/frontend/public/images/";
        Path imagePath = Paths.get(path + imageFileName);
        try {
            Files.write(imagePath, img.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(img.getOriginalFilename());
        return imageFileName;
    }
}
