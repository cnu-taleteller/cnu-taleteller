package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Reply;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class ReplyDto {

    private String memberEmail;
    private String replyContent;
    private Timestamp replyRegdate;
    private Book bookId;
    private Member member;

    @Builder
    public ReplyDto(String memberEmail, String replyContent, Timestamp replyRegdate, Book bookId, Member member) {
        this.memberEmail = memberEmail;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
        this.bookId = bookId;
        this.member = member;
    }
    public Reply toEntity() {
        return Reply.builder()
                .replyContent(replyContent)
                .replyRegdate(replyRegdate)
                .bookId(bookId)
                .member(member)
                .build();
    }

}