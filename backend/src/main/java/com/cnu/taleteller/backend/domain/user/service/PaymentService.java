package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.user.Repository.PaymentRepository;
import com.cnu.taleteller.backend.domain.user.domain.Payment;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    PaymentDto paymentDto = new PaymentDto();
    public void chargePoint(PaymentDto paymentDto) {
        System.out.println(paymentDto.getPayCount());
        Payment payment = Payment.toPayment(paymentDto);
        paymentRepository.save(payment);
    }

    public void setPaymentByPayCount(int point) {
        paymentDto.setPayCount(point);
        System.out.println(paymentDto.getPayCount());
        chargePoint(paymentDto);
    }
}
