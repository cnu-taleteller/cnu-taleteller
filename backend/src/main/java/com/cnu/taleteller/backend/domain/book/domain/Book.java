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
    private Long book_id;

    @Column(length = 100)
    private String book_name;

    private Timestamp book_regdate;

    @Column(length = 10)
    private String book_size;

    @Column(columnDefinition = "TEXT")
    private String book_description;

    @Column(length = 15)
    private String book_status;

    @Column(length = 15)
    private String book_category;

    private int book_recommend;

    @Column(length = 1)
    private String book_public;

}
