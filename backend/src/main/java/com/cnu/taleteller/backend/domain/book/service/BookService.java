package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.dto.BookmarkDto;
import com.cnu.taleteller.backend.domain.book.dto.RecommendDto;
import com.cnu.taleteller.backend.domain.book.entity.Bookmark;
import com.cnu.taleteller.backend.domain.book.entity.Recommend;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.repository.BookmarkRepository;
import com.cnu.taleteller.backend.domain.book.repository.RecommendRepository;
import com.cnu.taleteller.backend.domain.tool.entity.BookMongo;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.repository.BookMongoRepository;
import com.cnu.taleteller.backend.domain.tool.repository.UploadFileRepository;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.dto.BookManagementDto;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private final UploadFileRepository uploadFileRepository;
    private final RecommendRepository recommendRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<Book> searchByTitle(String keyword) {
        return bookRepository.findByBookNameContaining(keyword);
    }

    public List<Book> searchByName(String keyword) {
        return bookRepository.findByMember_MemberNameContaining(keyword);
    }

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

    @Transactional
    public Book saveThumbnailScenario(BookDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("err"));

        book.updateThumbnailScenario(dto.getScenario(), dto.getBookThumbnail());
        return bookRepository.save(book);
    }

    public String getScenario(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new IllegalArgumentException("err"));

        return book.getScenario();
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

    public Recommend recommendBook(Long bookId, RecommendDto recommendDto) {
        Member member = memberRepository.findByMemberEmail(recommendDto.getMemberEmail()).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if(member != null && book != null) {
            Recommend recommend = recommendRepository.findByMemberAndBookId(member, book);
            if (recommend != null && recommend.getMember() == member) {
                unrecommendBook(book.getBookId(), recommendDto);
            }
            else {
                recommendDto.setMember(member);
                recommendDto.setBookId(book);
                book.incrementRecommend();

                recommend = recommendRepository.save(recommendDto.toEntity());
                return recommend;
            }
        }
        return null;
    }

    public Recommend unrecommendBook(Long bookId, RecommendDto recommendDto) {
        Member member = memberRepository.findByMemberEmail(recommendDto.getMemberEmail()).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (member != null && book != null) {
            Recommend recommend = recommendRepository.findByMemberAndBookId(member, book);
            if (recommend != null) {
                book.decrementRecommend();
                recommendRepository.delete(recommend);
                return recommend;
            }
        }
        return null;
    }

    public Bookmark bookmarkBook(Long bookId, BookmarkDto bookmarkDto) {
        Member member = memberRepository.findByMemberEmail(bookmarkDto.getMemberEmail()).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (member != null && book != null) {
            Bookmark bookmark = bookmarkRepository.findByMemberAndBook(member, book);
            if (bookmark != null && bookmark.getMember() == member) {
                unbookmarkBook(book.getBookId(), bookmarkDto);
            } else {
                bookmarkDto.setMember(member);
                bookmarkDto.setBook(book);
                bookmark = bookmarkRepository.save(bookmarkDto.toEntity());
                return bookmark;
            }
        }
        return null;
    }

    public Bookmark unbookmarkBook(Long bookId, BookmarkDto bookmarkDto) {
        Member member = memberRepository.findByMemberEmail(bookmarkDto.getMemberEmail()).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (member != null && book != null) {
            Bookmark bookmark = bookmarkRepository.findByMemberAndBook(member, book);
            if (bookmark != null) {
                bookmarkRepository.delete(bookmark);
                return bookmark;
            }
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllNonTempBooks() {
        return bookRepository.findByBookStatusNot("temp");
    }

    public Book setPublic(Long bookId, BookManagementDto bookManagementDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("책 없음"));

        book.updatePublic(bookId, bookManagementDto.getBookPublic());
        return bookRepository.save(book);
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
    public List<Book> findUserBookList(String userEmail) {
        List<Book> bookList = bookRepository.findMyBookList(userEmail);
        return bookList != null ? bookList : Collections.emptyList();
    }

    @Transactional
    public boolean deleteBookList(List<Long> selectedBookList) {
        for (Long bookId : selectedBookList) {
            Optional<Book> book = bookRepository.findByBookId(bookId);

            if (book.isPresent()) {

                log.info("---" + book.get().getBookId());

                String mongoId = book.get().getBookMongo().getMongoId();
                CompletableFuture<Void> work = toolService.deleteBook(mongoId);

                List<UploadFile> uploadFiles = uploadFileRepository.findAllByBookId(bookId);

                if (!uploadFiles.isEmpty()) {
                    uploadFileRepository.deleteAll(uploadFiles);
                }

                bookRepository.deleteById(bookId);

                Long bookMongoId = book.get().getBookMongo().getBookMongoId();
                log.info("---" + bookMongoId);
                bookMongoRepository.deleteByBookMongoId(bookMongoId);

                try {
                    work.get();
                } catch (InterruptedException | ExecutionException e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.info("-------" + e);
                    return false;
                }
            }
        }
        return true;

    }
}
