package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bookRepository extends JpaRepository<Book, Long> {
}
