package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UploadFileDto {

    private String uploadFileName;
    private String uploadFileType;
    private Book book;

    @Builder
    public UploadFileDto(String uploadFileName, String uploadFileType, Book book) {
        this.uploadFileName = uploadFileName;
        this.uploadFileType = uploadFileType;
        this.book = book;
    }

    public UploadFile toEntity(){
        return UploadFile.builder()
                .uploadFileName(this.uploadFileName)
                .uploadFileType(this.uploadFileType)
                .book(book)
                .build();
    }
}
