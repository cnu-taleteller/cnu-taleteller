package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByBookId(Book bookId);
}