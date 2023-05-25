package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDataDTO {
    Page[] PageList;

    public BookData toEntity() {
        return BookData.builder()
                .pageList(PageList)
                .build();
    }
}
