package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/tool")
@AllArgsConstructor
public class ToolController {

    private final ScenarioService scenarioService;

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