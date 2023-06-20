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
public class PaymentDto {

    public int chargePoint; //전달받은 엽전 충전량
    public boolean chargeCheck; //유효 충전 체크(중복 충전 방지)
    public int returnPoint; //전달받은 엽전 환급량
    private Long payId; //내역번호
    private String paySort; //구분
    private LocalDateTime payDate; //일자
    private  int payCount; //개수
    private String payType; //수단
    private String payDetails; //상세내역

    public Long memberId;
    private Long bookId;

    @Builder
    public PaymentDto(Long payId, String paySort, int payCount, String payType, String payDetails, Long memberId, Long bookId) {
        this.payId = payId;
        this.paySort = paySort;
        this.payCount = payCount;
        this.payType = payType;
        this.payDetails = payDetails;
        this.memberId = memberId;
        this.bookId = bookId;
    }

    //Entity 생성 시 연관관계가 있는 객체(Member)들을 파라미터로 받음(엽전 충전)
    public Payment toEntity(Member member) {
        return Payment.builder()
                .payCount(payCount)
                .paySort(paySort)
                .payType(payType)
                .memberId(member)
                .build();
    }

}
