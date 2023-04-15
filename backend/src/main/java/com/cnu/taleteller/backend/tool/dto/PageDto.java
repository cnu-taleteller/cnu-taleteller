package com.cnu.taleteller.backend.tool.dto;
import com.cnu.taleteller.backend.tool.domain.Book;
import com.cnu.taleteller.backend.tool.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageDto {

    private Long page_id;

    private int page_sequence;

    private String page_status;

    private int caption_size;

    private String caption_content;

    private String caption_location;

    private String caption_color;

    private String thumbnail;

    private Long book_id;

    public Page toEntity() {
        return Page.builder()
                .page_id(this.page_id)
                .page_sequence(this.page_sequence)
                .page_status(this.page_status)
                .caption_size(this.caption_size)
                .caption_content(this.caption_content)
                .caption_location(this.caption_location)
                .caption_color(this.caption_color)
                .thumbnail(this.thumbnail)
                .book_id(Book.builder().book_id(this.book_id).build())
                .build();
    }
}