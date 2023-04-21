package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.user.domain.Payment;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import com.cnu.taleteller.backend.domain.user.dto.Point;
import com.cnu.taleteller.backend.domain.user.service.PaymentService;
import com.cnu.taleteller.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public String charge(@RequestBody Point point) {
        //PaymentDto paymentDto = new PaymentDto();
        System.out.println("충전포인트 = " + point.point);
        paymentService.setPaymentByPayCount(point.point);
       // paymentService.chargePoint(paymentDto);
        return "ok";
    }
}
