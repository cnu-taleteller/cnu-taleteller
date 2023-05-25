package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TTSDto {
    private String text;
    private String language;
    private String voice;
}
