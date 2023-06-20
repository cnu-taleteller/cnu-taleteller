package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.BookMongo;
import com.cnu.taleteller.backend.domain.tool.repository.BookMongoRepository;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BookMongoRepository bookMongoRepository;
    private final ToolService toolService;

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

    @Transactional
    public Long saveBook(BookTempSaveDto dto, String objectId) {

        Member member = memberRepository.findByMemberEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("err"));

        BookMongo bookMongo = BookMongo.builder()
                .mongoId(objectId)
                .build();
        bookMongoRepository.save(bookMongo);

        Book bookEntity = Book.builder()
                .bookName(dto.getBookName())
                .bookStatus(dto.getBookStatus())
                .member(member)
                .bookMongo(bookMongo)
                .build();
        Book savedBook = bookRepository.save(bookEntity);

        return savedBook.getBookId();
    }

    @Transactional
    public Book submitBook(BookDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("err"));
        book.updateSubmit(dto.getBookName(),
                dto.getBookDescription(),
                dto.getBookStatus(),
                dto.getBookCategory(),
                dto.getBookPublic());
        return bookRepository.save(book);
    }


    public Book saveThumbnail(BookDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("err"));
        book.updateThumbnail(dto.getBookThumbnail());
        return bookRepository.save(book);
    }

    @Transactional
    public Long updateBook(BookTempSaveDto dto, Long bookId) {
        CompletableFuture<Void> work = toolService.updateBookPages(bookId, dto.getPageList());
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("err"));
        book.update(dto.getBookName(), dto.getBookStatus());
        try {
            work.get();
        } catch (InterruptedException | ExecutionException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("-------" + e);
        }
        Book savedBook = bookRepository.save(book);
        return savedBook.getBookId();
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

    public List<Book> findAllMyWork(String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        return bookRepository.findAllMyWork(memId);
    }

    public List<Book> findAllMyBookmark(String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        return bookRepository.findAllMyBookmark(memId);
    }

    public List<Book> findAllMyPaywork(String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        return bookRepository.findAllMyPaywork(memId);
    }

    @Transactional(readOnly = true)
    public List<Long> findUserBookList(String userEmail) {
        List<Long> bookList = bookRepository.findMyBookList(userEmail);
        return bookList != null ? bookList : Collections.emptyList();
    }
}
