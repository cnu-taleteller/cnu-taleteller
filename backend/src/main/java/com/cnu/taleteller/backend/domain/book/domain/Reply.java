package com.cnu.taleteller.backend.domain.book.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "replys")
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long replyId;

    @Column(length = 600)
    private String replyContent;

    private Timestamp replyRegdate;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;

    @Builder
    public Reply(Long replyId, String replyContent, Timestamp replyRegdate, Book bookId) {
        this.replyId = replyId;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
        this.bookId = bookId;
    }
}