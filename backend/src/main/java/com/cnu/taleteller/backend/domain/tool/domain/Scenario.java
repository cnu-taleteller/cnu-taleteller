package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "scenarios")
@NoArgsConstructor
public class Scenario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long scenarioId;

    @Column(length = 100)
    private String scenarioContent;

    @Column(length = 10)
    private String scenarioType;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book bookId;

    @Builder
    public Scenario(Long scenarioId, String scenarioContent, String scenarioType, Book bookId) {
        this.scenarioId = scenarioId;
        this.scenarioContent = scenarioContent;
        this.scenarioType = scenarioType;
        this.bookId = bookId;
    }


}
