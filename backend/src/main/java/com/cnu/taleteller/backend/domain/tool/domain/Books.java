package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.user.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
public class Books {
    @Id
    private ObjectId id;

    private Page[] pageList;

    @Builder
    public Books(ObjectId id, Page[] pageList) {
        this.id = id;
        this.pageList = pageList;
    }
}
