package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.Scenario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioDto {

    private String scenarioContent;

    private String scenarioType;

    private Book book;


    @Builder
    public ScenarioDto(String scenarioContent, String scenarioType, Book book) {
        this.scenarioContent = scenarioContent;
        this.scenarioType = scenarioType;
        this.book = book;
    }

    public Scenario toEntity() {
        return Scenario.builder()
                .scenarioContent(this.scenarioContent)
                .scenarioType(this.scenarioType)
                .book(book)
                .build();
    }

}
