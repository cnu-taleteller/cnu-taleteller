package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> searchByTitle(String keyword) {
        return bookRepository.findByBookNameContaining(keyword);
    }

/*    public List<Book> searchByName(String keyword) {
        return bookRepository.findByUserNameContaining(keyword);
    }*/

    public List<Book> searchByContent(String keyword) {
        return bookRepository.findByBookDescriptionContaining(keyword);
    }

    public Book getBookByBookId(Long bookId) {
        return bookRepository.findByBookId(bookId);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public BookDto recommendBook(Long bookId, BookDto bookDto) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.incrementRecommend();
            bookRepository.save(book);
            return BookDto.fromEntity(book);
        }
        return null;
    }
}