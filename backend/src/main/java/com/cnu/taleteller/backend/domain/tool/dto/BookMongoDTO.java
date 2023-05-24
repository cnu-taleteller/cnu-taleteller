package com.cnu.taleteller.backend.domain.tool.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
public class BookMongoDTO {
    private Long bookMongoId;

    private ObjectId mongoId;

}
