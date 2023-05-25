package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileDto;
import com.cnu.taleteller.backend.domain.tool.dto.UploadFileRequestDto;
import com.cnu.taleteller.backend.domain.tool.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository fileRepository;

    @Transactional
    public List<UploadFile> save(UploadFileRequestDto dto, Long bookId) {
        List<UploadFile> uploadFiles = new ArrayList<>();

        if(dto.getUploadCharList()!=null) {
            List<String> uploadCharList = dto.getUploadCharList();
            for (String uploadChar : uploadCharList) {
                UploadFileDto uploadCharDto = UploadFileDto.builder()
                        .uploadFileName(uploadChar)
                        .uploadFileType("character")
                        .book(new Book(bookId))
                        .build();

                uploadFiles.add(uploadCharDto.toEntity());
            }
        }
       if(dto.getUploadBackList()!=null) {
           List<String> uploadBackList = dto.getUploadBackList();
           for (String uploadBack : uploadBackList) {
               UploadFileDto uploadBackDto = UploadFileDto.builder()
                       .uploadFileName(uploadBack)
                       .uploadFileType("background")
                       .book(new Book(bookId))
                       .build();

               uploadFiles.add(uploadBackDto.toEntity());
           }
       }
        
        List<UploadFile> list = fileRepository.saveAll(uploadFiles);

        return list;
    }
}
