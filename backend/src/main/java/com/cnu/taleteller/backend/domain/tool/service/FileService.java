package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.tool.domain.File;
import com.cnu.taleteller.backend.domain.tool.dto.FileDto;
import com.cnu.taleteller.backend.domain.tool.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    public String saveImage(MultipartFile img, String menu) {

        String uuid = UUID.randomUUID().toString();
        String imageFileName = uuid + "_" + img.getOriginalFilename();

        if(menu.equals("background")){
            imageFileName = "B_"+ uuid + "_" + img.getOriginalFilename();
        }
        else if(menu.equals("character")){
            imageFileName = "C_"+ uuid + "_" + img.getOriginalFilename();
        }

        // 파일을 로컬에 저장
        String savedPath = fileDir + imageFileName;
        try {
            img.transferTo(new java.io.File(savedPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 이미지 파일을 FileDto 객체로 변환
        FileDto fileDto = new FileDto();
        fileDto.setFileName(imageFileName);
        fileDto.setFileSize(String.valueOf(img.getSize()));
        fileDto.setFileOriginName(img.getOriginalFilename());

        // FileDto 객체를 이용하여 File 엔티티 객체 생성
        File file = fileDto.toEntity();
        fileRepository.save(file);

        return imageFileName;
    }
}
