package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.BookMongo;
import com.cnu.taleteller.backend.domain.tool.repository.BookMongoRepository;
import com.cnu.taleteller.backend.domain.tool.repository.ToolRepository;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
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
    public Book saveBook(String bookName, String bookStatus, String email, String objectId) {

        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("err"));

        BookMongo bookMongo = BookMongo.builder()
                .mongoId(objectId)
                .build();
        bookMongoRepository.save(bookMongo);

        Book bookEntity = Book.builder()
                .bookName(bookName)
                .bookStatus(bookStatus)
                .member(member)
                .bookMongo(bookMongo)
                .build();
        return bookRepository.save(bookEntity);
    }

    @Transactional
    public Book updateBook(BookTempSaveDto dto, Long bookId) throws ExecutionException, InterruptedException, RuntimeException {
        CompletableFuture<Void> work = toolService.updateBookPages(bookId, dto.getPageList());
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new IllegalArgumentException("err"));
        book.update(dto.getBookName(), dto.getBookStatus());
        work.get();
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
}