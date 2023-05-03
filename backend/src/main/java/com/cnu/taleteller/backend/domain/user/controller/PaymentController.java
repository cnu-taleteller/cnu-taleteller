package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.user.domain.Payment;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import com.cnu.taleteller.backend.domain.user.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(value = {"/wallet", "/details"})
    public ResponseEntity<List<Payment>> pointDetails(Model model) {
        List<Payment> pointDetails = paymentService.findAll();
        return ResponseEntity.ok(pointDetails);
    }

    @PostMapping("/chargeSet")
    public String chargeSet(@RequestBody PaymentDto point) {
        System.out.println("충전포인트 = " + point.chargePoint);
        paymentService.pointChargeSet(point.chargePoint);

        log.info("kakaoPay post............................................");
        String kakaopayUrl = paymentService.kakaopayReady();
        return kakaopayUrl;
    }

    @PostMapping("/chargeExecution")
    public void chargeExecution(){
        paymentService.pointChargeSave();
    }

    @PostMapping("/return")
    public void returnPoint(@RequestBody PaymentDto point){
        System.out.println("포인트환급 = " + point.returnPoint);
        paymentService.pointReturnSave(point.returnPoint);
    }

//    @GetMapping("/kakaoPay")
//    public void kakaoPayGet() {
//
//    }

//    @GetMapping("/kakaoPaySuccess")
//    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
//        log.info("kakaoPaySuccess get............................................");
//        log.info("kakaoPaySuccess pg_token : " + pg_token);
//
//    }

//    @GetMapping("/mypage/chargeResult")
//    public void kakaopayCharge(@RequestParam("pg_token") String pg_token){
//        log.info("kakaoPaySuccess get............................................");
//        log.info("kakaoPaySuccess pg_token : " + pg_token);
//        System.out.println(kakaopayService.kakaoPayInfo(pg_token));
//
//    }
}
