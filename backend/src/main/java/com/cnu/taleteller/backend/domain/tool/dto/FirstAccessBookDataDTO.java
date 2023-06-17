package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FirstAccessBookDataDTO {
    private BookData bookData;
    private String scenario;
    @Builder
    public FirstAccessBookDataDTO(BookData bookData, String scenario) {
        this.bookData = bookData;
        this.scenario = scenario;
    }
}
