package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

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
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new IllegalArgumentException("err"));
        return book;
    }

    public Book saveBook(BookTempSaveDto dto) {
        Member member = memberRepository.findByMemberEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("err"));
        dto.setMember(member);
        Book book = bookRepository.save(dto.toEntity());
        return book;
    }

    @Transactional
    public Book updateBook(BookTempSaveDto dto, Long bookId) {
        Book optionalBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("err"));

        Book book = optionalBook;
        book.update(bookId, dto.getBookName(), dto.getBookStatus());
        return bookRepository.save(book);
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