package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "pages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long page_id;

    private int page_sequence;

    @Column(length = 1)
    private String page_status;

    private int caption_size;

    @Column(columnDefinition = "TEXT")
    private String caption_content;

    @Column(length = 2)
    private String caption_location;

    @Column(length = 100)
    private String caption_color;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book_id;
}