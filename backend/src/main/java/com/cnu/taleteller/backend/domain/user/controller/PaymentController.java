package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.entity.Payment;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import com.cnu.taleteller.backend.domain.user.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(value = {"/wallet/{email}", "/details/{email}"})
    public ResponseEntity<List<Payment>> pointDetails(Model model, @PathVariable String email) {
        List<Payment> pointDetails = paymentService.findAllMyPayList(email);
        return ResponseEntity.ok(pointDetails);
    }

    @PostMapping("/chargeSet/{email}")
    public String chargeSet(@RequestBody Map<String, String> chargeInfo, @PathVariable String email) {

        System.out.println("충전포인트 = " + chargeInfo.get("chargePoint"));
        System.out.println("결제수단 = " + chargeInfo.get("paymentMethod"));
        int chargePoint = Integer.parseInt(chargeInfo.get("chargePoint"));
        String paymentMethod = chargeInfo.get("paymentMethod");
        paymentService.pointChargeSet(chargePoint,paymentMethod,email);

        log.info("kakaoPay post............................................");
        String kakaopayUrl = paymentService.kakaopayReady();
        return kakaopayUrl;
    }

    @GetMapping("/chargeExecution")
    public ResponseEntity<List<Payment>> chargeExecution(){
        paymentService.pointChargeSave();
        List<Payment> lastDetails = paymentService.findByLast();
        return ResponseEntity.ok(lastDetails);
    }

    @PostMapping("/bookPayment")
    public void bookPayment(@RequestBody Map<String, String> paymentInfo){
        System.out.println("결제액수 = " + paymentInfo.get("paymentPoint"));
        int paymentPoint = Integer.parseInt(paymentInfo.get("paymentPoint"));
        paymentService.bookPaymentSave(paymentPoint);
    }

    @PostMapping("/return/{email}")
    public int returnPoint(@RequestBody PaymentDto point, @PathVariable String email){
        System.out.println("포인트환급 = " + point.returnPoint);
        paymentService.pointReturnSave(point.returnPoint, email);

        return Math.abs(point.returnPoint);
    }

//    @GetMapping("/kakaoPay")
//    public void kakaoPayGet() {
//
//    }

//    @GetMapping("/mypage/chargeResult")
//    //@CrossOrigin(origins = "http://localhost:8200/")
//    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
//        log.info("kakaoPaySuccess get............................................");
//        log.info("kakaoPaySuccess pg_token : " + pg_token);
//
//        model.addAttribute("info", paymentService.kakaopayInfo(pg_token));
//        System.out.println(model);
//    }

//    @GetMapping("/mypage/chargeResult")
//    public void kakaopayCharge(@RequestParam("pg_token") String pg_token){
//        log.info("kakaoPaySuccess get............................................");
//        log.info("kakaoPaySuccess pg_token : " + pg_token);
//        System.out.println(kakaopayService.kakaoPayInfo(pg_token));
//
//    }
}
