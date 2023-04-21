package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioDto {

    private Long scenarioId;

    private String scenarioContent;

    private String scenarioType;

    private Long bookId;

    public Scenario toEntity() {
        return Scenario.builder()
                .scenarioId(this.scenarioId)
                .scenarioContent(this.scenarioContent)
                .scenarioType(this.scenarioType)
                .book(Book.builder().bookId(this.bookId).build())
                .build();
    }
}
