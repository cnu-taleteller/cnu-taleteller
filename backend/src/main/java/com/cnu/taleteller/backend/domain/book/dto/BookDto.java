package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;

import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String bookPublic;

    private Long memberId;

    public static BookDto fromEntity(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setBookRecommend(book.getBookRecommend());
        bookDto.setBookId(book.getBookId());
        bookDto.setBookName(book.getBookName());
        bookDto.setBookRegdate(book.getBookRegdate());
        bookDto.setBookDescription(book.getBookDescription());
        bookDto.setBookStatus(book.getBookStatus());
        bookDto.setBookCategory(book.getBookCategory());
        bookDto.setBookPublic(book.getBookPublic());
        return bookDto;
    }

}