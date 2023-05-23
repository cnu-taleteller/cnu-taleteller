package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.BookMongo;
import com.cnu.taleteller.backend.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByBookNameContaining(String keyword);

    // 회원 DB 수정해야 돼서 아직 작성자 검색 안 됨
    // List<Book> findByUserNameContaining(String keyword);
    List<Book> findByBookDescriptionContaining(String keyword);

    Optional<Book> findByBookId(Long bookId);

    @Query("SELECT bm.mongoId FROM Book b RIGHT JOIN b.bookMongo bm WHERE b.bookId = :bookId")
    String findMongoIdByBookId(@Param("bookId") Long bookId);

    Book save(Book book);
}