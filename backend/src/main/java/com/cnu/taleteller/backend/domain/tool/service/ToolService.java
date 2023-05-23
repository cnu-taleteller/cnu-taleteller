package com.cnu.taleteller.backend.domain.tool.service;

<<<<<<< Updated upstream
import com.cnu.taleteller.backend.domain.tool.domain.Tool;
=======
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.tool.domain.Books;
import com.cnu.taleteller.backend.domain.tool.domain.Page;
import com.cnu.taleteller.backend.domain.tool.dto.BooksDTO;
>>>>>>> Stashed changes
import com.cnu.taleteller.backend.domain.tool.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.transaction.Transactional;
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
            BooksDTO dto = new BooksDTO();
            dto.setPageList(pageList);
            Books book = toolRepository.save(dto.toEntity());
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
                Books existingBook = mongoTemplate.findOne(query, Books.class);

                if (existingBook == null) {
                    throw new IllegalArgumentException("Book not found");
                }

                BooksDTO updatedDto = new BooksDTO();
                updatedDto.setPageList(updatedPages);

                existingBook.setPageList(updatedDto.getPageList());

                toolRepository.save(existingBook);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception e) {
            throw new RuntimeException("RuntimeException rollback");
        }
    }

}
