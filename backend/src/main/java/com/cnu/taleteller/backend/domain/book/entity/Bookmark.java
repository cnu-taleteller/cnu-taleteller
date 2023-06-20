package com.cnu.taleteller.backend.domain.book.entity;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "bookmark")
@IdClass(BookmarkId.class)
@NoArgsConstructor
public class Bookmark implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Bookmark(Book book, Member member) {
        this.book = book;
        this.member = member;
    }
}
