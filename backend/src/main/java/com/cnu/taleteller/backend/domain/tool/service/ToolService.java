package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.BookData;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.Page;
import com.cnu.taleteller.backend.domain.tool.dto.BookDataDTO;
import com.cnu.taleteller.backend.domain.tool.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
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
public class ToolService {
    private final ToolRepository toolRepository;
    private final BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    //@Transactional(rollbackFor = {RuntimeException.class, Error.class}) 기본속성
    public ObjectId saveBook(Page[] pageList) {
        try {
            BookDataDTO dto = new BookDataDTO();
            dto.setPageList(pageList);
            BookData book = toolRepository.save(dto.toEntity());
            return book.getId();
        } catch (Exception e) {
            throw new RuntimeException("RuntimeException rollback");
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Void> updateBookPages(Long bookId, Page[] updatedPages) {
        try {
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
        } catch(Exception e) {
            throw new RuntimeException("RuntimeException rollback");
        }
    }

    @Transactional(readOnly = true)
    public BookData firstAccessData(Long bookId) {
        try {
            String mongodbId = bookRepository.findMongoIdByBookId(bookId);
            System.out.println(mongodbId);
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
        } catch(Exception e) {
            throw new RuntimeException("RuntimeException rollback");
        }
    }

    @Transactional
    public Book saveScenario(String scenario, Long bookId) {
        Book optionalBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("err"));

        Book book = optionalBook;
        book.updateScenario(scenario);
        return bookRepository.save(book);
    }


}
