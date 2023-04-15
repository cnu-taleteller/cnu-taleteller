package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class BookDto {

    private Long book_id;

    private String book_name;

    private Timestamp book_regdate;

    private String book_size;

    private String book_description;

    private String book_status;

    private String book_category;

    private int book_recommend;

    private String book_public;

    public Book toEntity() {
        return Book.builder()
                .book_id(this.book_id)
                .book_name(this.book_name)
                .book_regdate(this.book_regdate)
                .book_size(this.book_size)
                .book_description(this.book_description)
                .book_status(this.book_status)
                .book_category(this.book_category)
                .book_recommend(this.book_recommend)
                .book_public(this.book_public)
                .build();
    }

}