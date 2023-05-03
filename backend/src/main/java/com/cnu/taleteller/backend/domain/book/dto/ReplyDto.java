package com.cnu.taleteller.backend.domain.book.dto;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.book.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class ReplyDto {

    private Long replyId;
    private String replyContent;
    private Timestamp replyRegdate;
    private Book bookId;

    public Reply toEntity() {
        return Reply.builder()
                .replyId(this.replyId)
                .replyContent(this.replyContent)
                .replyRegdate(this.replyRegdate)
                .bookId(this.bookId)
                .build();
    }

    public static ReplyDto fromEntity(Reply reply) {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setReplyId(reply.getReplyId());
        replyDto.setReplyContent(reply.getReplyContent());
        replyDto.setReplyRegdate(reply.getReplyRegdate());
        replyDto.setBookId(reply.getBookId());
        return replyDto;
    }
}