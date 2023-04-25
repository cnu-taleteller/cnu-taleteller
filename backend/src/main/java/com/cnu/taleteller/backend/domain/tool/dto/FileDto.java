package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class FileDto {

    private Long fileId;

    private String fileName;

    private String fileSize;

    private String fileOriginName;

    private Long bookId;

    public File toEntity(){
        return File.builder()
                .fileId(this.fileId)
                .fileName(this.fileName)
                .fileSize(this.fileSize)
                .fileOriginName(this.fileOriginName)
                .bookId(Book.builder().bookId(this.bookId).build())
                .build();
    }
}
