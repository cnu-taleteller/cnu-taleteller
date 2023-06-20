package com.cnu.taleteller.backend.domain.user.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookManagementDto {
    private String bookPublic;

    @Builder
    public BookManagementDto(String bookPublic) {
        this.bookPublic = bookPublic;
    }

    public Book toEntity(){
        return Book.builder().bookPublic(bookPublic).build();
    }
}