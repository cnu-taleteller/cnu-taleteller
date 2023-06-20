package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;

import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BookDto {

    private Long bookId;

    private String bookName;

    private LocalDateTime bookRegdate;

    private String bookDescription;

    private String bookStatus;

    private String bookCategory;

    private int bookRecommend;

    private String bookThumbnail;
    private String scenario;
    private String bookPublic;

    private Member member;

    @Builder
    public BookDto(String bookName, LocalDateTime bookRegdate, String bookDescription, String bookStatus, String bookCategory, int bookRecommend, String bookPublic, Member member) {
        this.bookName = bookName;
        this.bookRegdate = bookRegdate;
        this.bookDescription = bookDescription;
        this.bookStatus = bookStatus;
        this.bookCategory = bookCategory;
        this.bookRecommend = bookRecommend;
        this.bookPublic = bookPublic;
        this.member = member;
    }

    public Book toEntity(){
        return Book.builder()
                .bookName(bookName)
                .bookRegdate(bookRegdate)
                .bookDescription(bookDescription)
                .bookStatus(bookStatus)
                .bookCategory(bookCategory)
                .bookRecommend(bookRecommend)
                .bookPublic(bookPublic)
                .member(member)
                .build();
    }
}