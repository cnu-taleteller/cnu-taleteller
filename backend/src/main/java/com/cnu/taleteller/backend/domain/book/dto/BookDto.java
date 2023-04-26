package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class BookDto {

    private Long bookId;

    private String bookName;

    private Timestamp bookRegdate;

    private String bookSize;

    private String bookDescription;

    private String bookStatus;

    private String bookCategory;

    private int bookRecommend;

    private String bookPublic;

    public Book toEntity() {
        return Book.builder()
                .bookId(this.bookId)
                .bookName(this.bookName)
                .bookRegdate(this.bookRegdate)
                .bookSize(this.bookSize)
                .bookDescription(this.bookDescription)
                .bookStatus(this.bookStatus)
                .bookCategory(this.bookCategory)
                .bookRecommend(this.bookRecommend)
                .bookPublic(this.bookPublic)
                .build();
    }

    public static BookDto fromEntity(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setBookRecommend(book.getBookRecommend());

        bookDto.setBookId(book.getBookId());
        bookDto.setBookName(book.getBookName());
        bookDto.setBookRegdate(book.getBookRegdate());
        bookDto.setBookSize(book.getBookSize());
        bookDto.setBookDescription(book.getBookDescription());
        bookDto.setBookStatus(book.getBookStatus());
        bookDto.setBookCategory(book.getBookCategory());
        bookDto.setBookPublic(book.getBookPublic());
        return bookDto;
    }
}