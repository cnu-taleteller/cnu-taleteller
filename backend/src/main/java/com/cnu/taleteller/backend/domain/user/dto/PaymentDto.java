package com.cnu.taleteller.backend.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {

    private Long payId; //내역번호
    private String paySort; //구분
    private LocalDateTime payDate; //일자
    private int payCount; //개수
    private String payType; //수단
    private String payDetails; //상세내역

}
