package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.Page;
import com.cnu.taleteller.backend.domain.tool.dto.BookDataDTO;
import com.cnu.taleteller.backend.domain.tool.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToolService {
    private final ToolRepository toolRepository;
    private final BookRepository bookRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    public ObjectId saveBook(Page[] pageList) {
        BookDataDTO dto = new BookDataDTO();
        dto.setPageList(pageList);
        BookData book = toolRepository.save(dto.toEntity());
        return book.getId();
    }

    @Async
    @Transactional
    public CompletableFuture<Void> updateBookPages(Long bookId, Page[] updatedPages) {
        String mongodbId = bookRepository.findMongoIdByBookId(bookId);
        if (mongodbId != null) {
            ObjectId objectId = new ObjectId(mongodbId);

            Query query = new Query(Criteria.where("_id").is(objectId));
            BookData existingBook = mongoTemplate.findOne(query, BookData.class);

            if (existingBook == null) {
                throw new IllegalArgumentException("Book not found");
            }

            BookDataDTO updatedDto = new BookDataDTO();
            updatedDto.setPageList(updatedPages);

            existingBook.setPageList(updatedDto.getPageList());

            toolRepository.save(existingBook);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional(readOnly = true)
    public BookData firstAccessData(Long bookId) {
        String mongodbId = bookRepository.findMongoIdByBookId(bookId);

        log.info("---------" + mongodbId);

        if(mongodbId == null) {
            throw new NullPointerException("mongodbId is null");
        }

        ObjectId objectId = new ObjectId(mongodbId);

        Query query = new Query(Criteria.where("_id").is(objectId));
        BookData existingBook = mongoTemplate.findOne(query, BookData.class);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }
        return existingBook;
    }

}
