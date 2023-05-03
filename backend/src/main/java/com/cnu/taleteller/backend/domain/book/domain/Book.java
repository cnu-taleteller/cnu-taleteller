package com.cnu.taleteller.backend.domain.book.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bookId;

    @Column(length = 100)
    private String bookName;

    private Timestamp bookRegdate;

    @Column(length = 10)
    private String bookSize;

    @Column(columnDefinition = "TEXT")
    private String bookDescription;

    @Column(length = 15)
    private String bookStatus;

    @Column(length = 15)
    private String bookCategory;

    private int bookRecommend;

    @Column(length = 1)
    private String bookPublic;

    @Builder
    public Book(Long bookId, String bookName, Timestamp bookRegdate, String bookSize, String bookDescription, String bookStatus, String bookCategory, int bookRecommend, String bookPublic) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookRegdate = bookRegdate;
        this.bookSize = bookSize;
        this.bookDescription = bookDescription;
        this.bookStatus = bookStatus;
        this.bookCategory = bookCategory;
        this.bookRecommend = bookRecommend;
        this.bookPublic = bookPublic;
    }

    public void incrementRecommend() {
       this.bookRecommend = bookRecommend + 1;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        // 로그인 기능 되면 사용자 이름도 추가
    }
}