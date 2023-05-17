package com.cnu.taleteller.backend.domain.tool.entity;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "files")
@NoArgsConstructor
@ToString
public class UploadFile {

    @Id
    @Column(length = 200)
    private String uploadFileName;

    @Column(length = 200)
    private String uploadFileType;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @Builder
    public UploadFile(String uploadFileName, String uploadFileType, Book book) {
        this.uploadFileName = uploadFileName;
        this.uploadFileType = uploadFileType;
        this.book = book;
    }
}