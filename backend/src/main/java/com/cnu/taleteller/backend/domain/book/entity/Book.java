package com.cnu.taleteller.backend.domain.book.entity;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
public class Book {

    @Id
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

    @Column(columnDefinition = "TEXT")
    private String scenario;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Book(Long bookId, String bookName, LocalDateTime bookRegdate, String bookDescription, String bookStatus, String bookCategory, int bookRecommend, String bookPublic,String scenario, Member member) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookRegdate = bookRegdate;
        this.bookDescription = bookDescription;
        this.bookStatus = bookStatus;
        this.bookCategory = bookCategory;
        this.bookRecommend = bookRecommend;
        this.bookPublic = bookPublic;
        this.scenario = scenario;
        this.member = member;
    }

    public void incrementRecommend() {
       this.bookRecommend = bookRecommend + 1;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        // 로그인 기능 되면 사용자 이름도 추가
    }

    public void update(String bookName, String bookStatus){
        this.bookName = bookName;
        this.bookStatus = bookStatus;
    }

    public void updateScenario(String scenario){
        this.scenario = scenario;
    }


    public Book(Long bookId) {
        this.bookId = bookId;
    }
}