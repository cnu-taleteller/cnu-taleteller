package com.cnu.taleteller.backend.domain.tool.entity;

import com.cnu.taleteller.backend.domain.book.entity.Book;
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
    private Book book;

    @Builder
    public Scenario(Long scenarioId, String scenarioContent, String scenarioType, Book book) {
        this.scenarioId = scenarioId;
        this.scenarioContent = scenarioContent;
        this.scenarioType = scenarioType;
        this.book = book;
    }

    public void update(String scenarioContent, String scenarioType) {
        this.scenarioContent = scenarioContent;
        this.scenarioType = scenarioType;
    }
}
