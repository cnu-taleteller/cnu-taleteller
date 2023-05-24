package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.tool.domain.Books;
import com.cnu.taleteller.backend.domain.tool.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BooksDTO {
    Page[] PageList;

    public Books toEntity() {
        return Books.builder()
                .pageList(PageList)
                .build();
    }
}
