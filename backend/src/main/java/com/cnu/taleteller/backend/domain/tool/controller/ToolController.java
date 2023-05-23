package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.domain.Books;
import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
<<<<<<< Updated upstream
import com.cnu.taleteller.backend.domain.tool.service.FileService;
=======
import com.cnu.taleteller.backend.domain.tool.domain.Tool;
import com.cnu.taleteller.backend.domain.tool.domain.UploadFile;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileRequestDto;
import com.cnu.taleteller.backend.domain.tool.service.UploadFileService;
import com.cnu.taleteller.backend.domain.tool.service.S3Service;
>>>>>>> Stashed changes
import com.cnu.taleteller.backend.domain.tool.service.ScenarioService;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ScenarioService scenarioService;
<<<<<<< Updated upstream

    private final FileService fileService;

=======
    private final UploadFileService uploadFileService;
    private final S3Service s3Service;
>>>>>>> Stashed changes
    private final ToolService toolService;

    @PostMapping("/scenario/{bookId}")
    public List<Scenario> saveScenario(@RequestBody String[] scenario, @PathVariable Long bookId){
        List<Scenario> list = scenarioService.save(scenario, bookId);
        return list;
    }

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
