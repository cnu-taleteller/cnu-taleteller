package com.cnu.taleteller.backend.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bookId;

    @Column(length = 100)
    private String bookName;

    private Timestamp bookRegdate;

    @Column(length = 10)
    private String bookSize;

    @Column(columnDefinition = "TEXT")
    private String bookDescription;

    @Column(length = 15)
    private String bookStatus;

    @Column(length = 15)
    private String bookCategory;

    private int bookRecommend;

    @Column(length = 1)
    private String bookPublic;

}