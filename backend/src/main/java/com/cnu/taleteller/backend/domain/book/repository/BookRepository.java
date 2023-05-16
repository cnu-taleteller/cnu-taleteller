package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByBookNameContaining(String keyword);

    // 회원 DB 수정해야 돼서 아직 작성자 검색 안 됨
    // List<Book> findByUserNameContaining(String keyword);

    List<Book> findByBookDescriptionContaining(String keyword);

    Book findByBookId(Long bookId);

    Book save(Book book);
}