package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Recommend;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RecommendDto {

    private String memberEmail;

    private Book bookId;

    private Member member;

    @Builder
    public RecommendDto(String memberEmail, Book bookId, Member member) {
        this.memberEmail = memberEmail;
        this.bookId = bookId;
        this.member = member;
    }

    public Recommend toEntity(){
        return Recommend.builder()
                .bookId(bookId)
                .member(member)
                .build();
    }
}
