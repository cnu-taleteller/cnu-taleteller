package com.cnu.taleteller.backend.domain.book.entity;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "replies")
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long replyId;

    @Column(length = 600)
    private String replyContent;

    @CreationTimestamp
    private Timestamp replyRegdate;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reply(Long replyId, String replyContent, Timestamp replyRegdate, Book bookId, Member member) {
        this.replyId = replyId;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
        this.bookId = bookId;
        this.member = member;
    }

    public void updateReply(Long replyId, String replyContent){
        this.replyId = replyId;
        this.replyContent = replyContent;
    }
}