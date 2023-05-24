package com.cnu.taleteller.backend.domain.tool.domain;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(name = "book_mongo_id_index",columnList = "book_mongo_id")})
@NoArgsConstructor
public class BookMongo implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "book_mongo_id")
    private Long bookMongoId;

    @Column(name = "mongodb_id")
    private String mongoId;

    @Builder
    public BookMongo(Long bookMongoId, String mongoId) {
        this.bookMongoId = bookMongoId;
        this.mongoId = mongoId;
    }
}
