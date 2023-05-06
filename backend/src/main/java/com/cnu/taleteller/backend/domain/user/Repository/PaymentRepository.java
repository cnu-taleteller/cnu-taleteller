package com.cnu.taleteller.backend.domain.user.Repository;

import com.cnu.taleteller.backend.domain.user.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
   List<Payment> findAll();

   List<Payment> findFirstByOrderByPayIdDesc();
}
