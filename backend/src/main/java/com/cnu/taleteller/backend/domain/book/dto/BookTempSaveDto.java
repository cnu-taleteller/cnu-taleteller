package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.BookMongo;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.Page;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookTempSaveDto {

    private String email;

    private String bookName;

    private LocalDateTime bookRegdate;

    private String bookStatus;

    private String bookThumbnail;

    private Member member;

    private BookMongo bookMongo;

    private Page[] pageList;

    @Builder
    public BookTempSaveDto(String email, String bookName, LocalDateTime bookRegdate, String bookThumbnail, String bookStatus, Member member) {
        this.email = email;
        this.bookName = bookName;
        this.bookRegdate = bookRegdate;
        this.bookStatus = bookStatus;
        this.bookThumbnail = bookThumbnail;
        this.member = member;
    }

    public Book toEntity(){
        return Book.builder()
                .bookName(bookName)
                .bookRegdate(bookRegdate)
                .bookStatus(bookStatus)
                .bookThumbnail(bookThumbnail)
                .member(member)
                .bookMongo(bookMongo)
                .build();
    }
}
