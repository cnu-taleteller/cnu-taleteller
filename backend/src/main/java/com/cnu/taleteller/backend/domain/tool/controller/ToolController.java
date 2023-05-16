package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.service.FileService;
import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tool")
@AllArgsConstructor
public class ToolController {

    private final ScenarioService scenarioService;
    private final FileService fileService;

    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile img, @RequestParam("menu") String menu) {
        try {
            if (img.isEmpty()) {
                throw new IllegalArgumentException("이미지 파일 오류");
            }
            if (menu == null || menu.isEmpty()) {
                throw new IllegalArgumentException("메뉴 오류");
            }
            String imageFileName = fileService.uploadFiles(img, menu, "static");
            if (imageFileName == null) {
                return ResponseEntity.badRequest().body("파일 업로드 실패");
            }
            return ResponseEntity.ok(imageFileName);
        } catch (Exception e) {
            String msg = "사진 업로드 실패: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }
}
//    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<String> saveImage (@RequestParam("image") MultipartFile img, @RequestParam("menu") String menu) {
//        try {
//            if (img.isEmpty()) {
//                throw new IllegalArgumentException("이미지 파일 오류");
//            }
//            if (menu == null || menu.isEmpty()) {
//                throw new IllegalArgumentException("메뉴 오류");
//            }
//            String imageFileName = fileService.saveImage(img, menu);
//            if (imageFileName == null) {
//                return ResponseEntity.badRequest().body("이미지 파일 업로드 실패");
//            }
//            return ResponseEntity.ok(imageFileName);
//        }
//        catch (Exception e) {
//            String msg = "사진 업로드 실패: " + e.getMessage();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
//        }
//    }
