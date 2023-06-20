package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Recommend;
import com.cnu.taleteller.backend.domain.book.entity.RecommendId;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, RecommendId> {
    Recommend findByMemberAndBookId(Member member, Book bookId);
}