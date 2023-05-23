package com.cnu.taleteller.backend.domain.book.domain;

import com.cnu.taleteller.backend.domain.tool.domain.BookMongo;
import com.cnu.taleteller.backend.domain.user.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "books", indexes = {@Index(name = "book_id_index",columnList = "book_id")})
@NoArgsConstructor
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bookId;

    @Column(length = 100)
    private String bookName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime bookRegdate;

    @Column(columnDefinition = "TEXT")
    private String bookDescription;

    @Column(length = 15)
    private String bookStatus;

    @Column(length = 15)
    private String bookCategory;

    private int bookRecommend;

    @Column(length = 1)
    private String bookPublic;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_mongo_id")
    @JsonIgnore
    private BookMongo bookMongo;

    @Builder
    public Book(Long bookId, String bookName, LocalDateTime bookRegdate, String bookDescription, String bookStatus, String bookCategory, int bookRecommend, String bookPublic, Member member, BookMongo bookMongo) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookRegdate = bookRegdate;
        this.bookDescription = bookDescription;
        this.bookStatus = bookStatus;
        this.bookCategory = bookCategory;
        this.bookRecommend = bookRecommend;
        this.bookPublic = bookPublic;
        this.member = member;
        this.bookMongo = bookMongo;
    }

    public void incrementRecommend() {
       this.bookRecommend = bookRecommend + 1;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        // 로그인 기능 되면 사용자 이름도 추가
    }

    public void update(Long bookId, String bookName, String bookStatus){
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookStatus = bookStatus;
    }

    public Book(Long bookId) {
        this.bookId = bookId;
    }
}