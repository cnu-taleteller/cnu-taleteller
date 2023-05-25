package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.dto.UploadVoiceRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.UploadVoiceDto;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadVoiceService {

    private final UploadFileRepository fileRepository;

    @Transactional
    public List<UploadFile> save(UploadVoiceRequestDto dto, Long bookId) {
        List<String> voiceList = dto.getVoiceList();
        List<UploadFile> uploadFiles = new ArrayList<>();

        for (String uploadChar : voiceList) {
            UploadVoiceDto uploadVoiceDto = UploadVoiceDto.builder()
                    .uploadFileName(uploadChar)
                    .uploadFileType("Voice")
                    .book(new Book(bookId))
                    .build();

            uploadFiles.add(uploadVoiceDto.toEntity());
        }

        List<UploadFile> list = fileRepository.saveAll(uploadFiles);

        return list;
    }
}
