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
    public Book recommendBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        BookDto bookDto = new BookDto();
        if (book != null) {
            bookDto.setBookRecommend(book.getBookRecommend() + 1);
            bookRepository.save(book);
        }
        return book;
    }
}