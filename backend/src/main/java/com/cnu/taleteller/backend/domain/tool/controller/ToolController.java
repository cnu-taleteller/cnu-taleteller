package com.cnu.taleteller.backend.domain.tool.controller;

import com.cnu.taleteller.backend.domain.tool.dto.TTSDto;
import com.cnu.taleteller.backend.domain.tool.dto.TTSResponse;
import com.cnu.taleteller.backend.domain.tool.dto.UploadVoiceRequestDto;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileRequestDto;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.tool.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tool")
@RequiredArgsConstructor
public class ToolController {

    private final UploadFileService uploadFileService;
    private final UploadVoiceService uploadVoiceService;
    private final S3Service s3Service;
    private final TTSService ttsService;

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

    @PostMapping("/uploadVoice/{bookId}")
    public ResponseEntity saveVoiceFile(@RequestBody UploadVoiceRequestDto dto, @PathVariable Long bookId){
        List<UploadFile> list = uploadVoiceService.save(dto, bookId);
        System.out.print(list.toString());
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/tts")
    public ResponseEntity<TTSResponse> generateTTS(@RequestBody TTSDto dto) throws IOException {
        System.out.println(dto.toString());
        String ttsUrl = ttsService.generateTTSUrl(dto);
        String encodedFileName = ttsService.getEncodedFileName();
        TTSResponse response = new TTSResponse(ttsUrl, encodedFileName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/firstAccess/{bookId}")
    public BookData firstAccess(@PathVariable Long bookId) {
        System.out.println(bookId);
        return toolService.firstAccessData(bookId);
    }
}
