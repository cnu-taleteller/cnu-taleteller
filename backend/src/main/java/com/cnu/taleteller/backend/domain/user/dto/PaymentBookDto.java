package com.cnu.taleteller.backend.domain.user.dto;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentBookDto {

    private String memberEmail;

    private String paySort; //구분
    private LocalDateTime payDate; //일자
    private  int payCount; //개수
    private String payType; //수단
    private String payDetails; //상세내역

    private Member memberId;
    private Book bookId;

    @Builder
    public PaymentBookDto(String memberEmail, String paySort, int payCount, String payType, String payDetails, Member memberId, Book bookId) {
        this.memberEmail = memberEmail;
        this.paySort = paySort;
        this.payCount = payCount;
        this.payType = payType;
        this.payDetails = payDetails;
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public Payment toEntity(){
        return Payment.builder()
                .payCount(payCount)
                .paySort(paySort)
                .payType(payType)
                .payDetails(payDetails)
                .memberId(memberId)
                .bookId(bookId)
                .build();
    }
}
