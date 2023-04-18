package com.cnu.taleteller.backend.domain.tool.dto;
import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageDto {

    private Long pageId;

    private int pageSequence;

    private String pageStatus;

    private int captionSize;

    private String captionContent;

    private String captionLocation;

    private String captionColor;

    private String thumbnail;

    private Long bookId;

    public Page toEntity() {
        return Page.builder()
                .pageId(this.pageId)
                .pageSequence(this.pageSequence)
                .pageStatus(this.pageStatus)
                .captionSize(this.captionSize)
                .captionContent(this.captionContent)
                .captionLocation(this.captionLocation)
                .captionColor(this.captionColor)
                .thumbnail(this.thumbnail)
                .bookId(Book.builder().bookId(this.bookId).build())
                .build();
    }
}