package com.cnu.taleteller.backend.domain.user.entity;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
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
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookId;

    @Builder
    public Payment(Long payId, String paySort, int payCount, String payType, String payDetails, Member memberId, Book bookId) {
        this.payId = payId;
        this.paySort = paySort;
        this.payCount = payCount;
        this.payType = payType;
        this.payDetails = payDetails;
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public static Payment toPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPayCount(paymentDto.getPayCount());
        payment.setPayDetails(paymentDto.getPayDetails());
        payment.setPayDate(paymentDto.getPayDate());
        payment.setPaySort(paymentDto.getPaySort());
        payment.setPayType(paymentDto.getPayType());
//        payment.setBookId(paymentDto.getBookId());
//        payment.setMemberId(paymentDto.getMemberId());
        return payment;
    }

}