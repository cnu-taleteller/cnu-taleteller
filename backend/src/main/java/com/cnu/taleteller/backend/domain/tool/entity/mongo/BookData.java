package com.cnu.taleteller.backend.domain.tool.entity.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
public class BookData {
    @Id
    private ObjectId id;

    private Page[] pageList;

    @Builder
    public BookData(ObjectId id, Page[] pageList) {
        this.id = id;
        this.pageList = pageList;
    }
}
