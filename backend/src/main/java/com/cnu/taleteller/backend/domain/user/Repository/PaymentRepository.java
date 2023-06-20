package com.cnu.taleteller.backend.domain.user.Repository;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
   List<Payment> findAll();

   List<Payment> findFirstByOrderByPayIdDesc();

//   @Query(value = "SELECT p.pay_sort, p.pay_date, p.pay_count, p.pay_type FROM Payment AS p", nativeQuery = true)
//   List<Payment> findLastMyPayList();

   @Query("SELECT p FROM Payment p Where p.memberId.memberId = :memberId ")
   List<Payment> findAllMyPayList(Long memberId);

//   Payment findByMemberAndBook(Member member, Book book);
}