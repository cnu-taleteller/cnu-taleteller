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

    // 회원 DB 수정해야 돼서 아직 작성자 검색 안 됨
    // List<Book> findByUserNameContaining(String keyword);
    List<Book> findByBookDescriptionContaining(String keyword);

    Optional<Book> findByBookId(Long bookId);

    @Query("SELECT bm.mongoId FROM Book b RIGHT JOIN b.bookMongo bm WHERE b.bookId = :bookId")
    String findMongoIdByBookId(@Param("bookId") Long bookId);

    @Query("SELECT b.bookId FROM Book b LEFT JOIN b.member m WHERE m.memberEmail = :userEmail")
    List<Long> findMyBookList(@Param("userEmail") String userEmail);

    Book save(Book book);

    @Query("SELECT b FROM Book b WHERE b.member.memberId = :memberId")
    List<Book> findAllMyWork(Long memberId);

    //3개 테이블 left join(books, bookmarks, members) : 해당 회원이 즐겨찾기한 작품
    @Query("SELECT b FROM Book b LEFT JOIN BookMark bm ON b.bookId = bm.book.bookId " +
            "LEFT JOIN Member m ON b.member.memberId = m.memberId " +
            "WHERE bm.member.memberId = :memberId")
    List<Book> findAllMyBookmark(Long memberId);

    //3개 테이블 left join(books, payment, members) : 해당 회원이 결제한 작품
    @Query("SELECT b FROM Book b LEFT JOIN Payment p ON b.bookId = p.bookId.bookId " +
            "LEFT JOIN Member m ON b.member.memberId = m.memberId " +
            "WHERE p.memberId.memberId = :memberId AND p.paySort = '결제'")
    List<Book>findAllMyPaywork(Long memberId);
}