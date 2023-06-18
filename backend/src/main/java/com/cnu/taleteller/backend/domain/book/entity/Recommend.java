package com.cnu.taleteller.backend.domain.book.entity;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "recommend")
@IdClass(RecommendId.class)
@NoArgsConstructor
public class Recommend implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Recommend(Book bookId, Member member) {
        this.bookId = bookId;
        this.member = member;
    }
}
