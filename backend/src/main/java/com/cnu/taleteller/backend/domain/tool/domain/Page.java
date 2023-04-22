package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "pages")
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pageId;

    private int pageNumber;

    @Column(length = 1)
    private String pageStatus;

    private int captionSize;

    @Column(columnDefinition = "TEXT")
    private String captionContent;

    @Column(length = 2)
    private String captionLocation;

    @Column(length = 100)
    private String captionColor;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book bookId;

    @Builder
    public Page(Long pageId, int pageNumber, String pageStatus, int captionSize, String captionContent, String captionLocation, String captionColor, String thumbnail, Book bookId) {
        this.pageId = pageId;
        this.pageNumber = pageNumber;
        this.pageStatus = pageStatus;
        this.captionSize = captionSize;
        this.captionContent = captionContent;
        this.captionLocation = captionLocation;
        this.captionColor = captionColor;
        this.thumbnail = thumbnail;
        this.bookId = bookId;
    }
}