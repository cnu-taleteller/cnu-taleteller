package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Bookmark;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookmarkDto {

    private String memberEmail;

    private Book book;

    private Member member;

    @Builder
    public BookmarkDto(String memberEmail, Book book, Member member) {
        this.memberEmail = memberEmail;
        this.book = book;
        this.member = member;
    }

    public Bookmark toEntity(){
        return Bookmark.builder()
                .book(book)
                .member(member)
                .build();
    }
}
