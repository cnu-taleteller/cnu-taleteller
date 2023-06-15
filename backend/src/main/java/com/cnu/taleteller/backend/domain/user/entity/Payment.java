package com.cnu.taleteller.backend.domain.user.entity;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId; //내역번호

    @Column(length = 15)
    private String paySort; //구분

    @Column
    private int payCount; //개수

    @Column(length = 15)
    private String payType; //수단

    @Column(length = 100)
    private String payDetails; //상세내역

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public static Payment toPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPayCount(paymentDto.getPayCount());
        payment.setPayDetails(paymentDto.getPayDetails());
        payment.setPayDate(paymentDto.getPayDate());
        payment.setPaySort(paymentDto.getPaySort());
        payment.setPayType(paymentDto.getPayType());
        return payment;
    }

}