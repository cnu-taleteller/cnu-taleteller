package com.cnu.taleteller.backend.domain.book.entity;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "bookmarks")
@NoArgsConstructor
public class BookMark {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Builder
    public BookMark(Long bookmarkId, Member member, Book book){
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.book = book;
    }
}
