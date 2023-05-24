package com.cnu.taleteller.backend.domain.tool.repository;

import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//MongoTemplate 을 사용해서 더 복잡한 쿼리를 처리 할 수 있다.
@Repository
public interface ToolRepository extends MongoRepository<BookData, String> {

}
