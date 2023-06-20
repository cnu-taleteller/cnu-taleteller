package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.entity.BookMongo;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByBookNameContaining(String keyword);

    List<Book> findByMember_MemberNameContaining(String keyword);

    List<Book> findByBookDescriptionContaining(String keyword);
    @Query("SELECT b.scenario FROM Book b WHERE b.bookId = :bookId")
    String findScenarioByBookId(@Param("bookId") Long bookId);

    Optional<Book> findByBookId(Long bookId);

    @Query("SELECT bm.mongoId FROM Book b RIGHT JOIN b.bookMongo bm WHERE b.bookId = :bookId")
    String findMongoIdByBookId(@Param("bookId") Long bookId);

    @Query("SELECT b FROM Book b LEFT JOIN b.member m WHERE m.memberEmail = :userEmail")
    List<Book> findMyBookList(@Param("userEmail") String userEmail);

    Book save(Book book);

    @Query("SELECT b FROM Book b WHERE b.member.memberId = :memberId")
    List<Book> findAllMyWork(Long memberId);

    //3개 테이블 left join(books, bookmark, members) : 해당 회원이 즐겨찾기한 작품
    @Query("SELECT b FROM Book b LEFT JOIN Bookmark bm ON b.bookId = bm.book.bookId " +
            "LEFT JOIN Member m ON b.member.memberId = m.memberId WHERE bm.member.memberId = :memberId")
    List<Book> findAllMyBookmark(@Param("memberId") Long memberId);

    List<Book> findByBookStatusNot(String status);

    List<Book> findByBookStatusNotAndBookPublicNot(String bookStatus, String bookPublic);

    //3개 테이블 left join(books, payment, members) : 해당 회원이 결제한 작품
    @Query("SELECT b FROM Book b LEFT JOIN Payment p ON b.bookId = p.bookId.bookId " +
            "LEFT JOIN Member m ON b.member.memberId = m.memberId " +
            "WHERE p.memberId.memberId = :memberId AND p.paySort = '결제'")
    List<Book>findAllMyPaywork(Long memberId);
}