package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "files")
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long fileId;

    @Column(length = 100)
    private String fileName;

    @Column(length = 10)
    private String fileSize;

    @Column(length = 100)
    private String fileOriginName;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book bookId;

    @Builder
    public File(Long fileId, String fileName, String fileSize, String fileOriginName, Book bookId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileOriginName = fileOriginName;
        this.bookId = bookId;
    }
}
