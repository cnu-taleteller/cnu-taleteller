package com.cnu.taleteller.backend.domain.user.dto;

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

}
