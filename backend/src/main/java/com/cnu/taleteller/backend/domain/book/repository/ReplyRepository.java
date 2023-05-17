package com.cnu.taleteller.backend.domain.book.repository;

import com.cnu.taleteller.backend.domain.book.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}