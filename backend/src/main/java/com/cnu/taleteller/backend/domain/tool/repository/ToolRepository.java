package com.cnu.taleteller.backend.domain.tool.repository;

import com.cnu.taleteller.backend.domain.tool.entity.Tool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends MongoRepository<Tool, String> {
}
