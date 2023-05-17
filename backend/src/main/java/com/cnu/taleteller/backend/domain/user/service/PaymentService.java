package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.user.Repository.PaymentRepository;
import com.cnu.taleteller.backend.domain.user.entity.Payment;
import com.cnu.taleteller.backend.domain.user.dto.KakaopayApprovalVO;
import com.cnu.taleteller.backend.domain.user.dto.KakaopayReadyVO;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log
public class PaymentService {

    private final PaymentRepository paymentRepository;
    PaymentDto paymentDto = new PaymentDto();

    private static final String HOST = "https://kapi.kakao.com";
    private KakaopayReadyVO kakaopayReadyVO;
    private KakaopayApprovalVO kakaopayApprovalVO;

    public void pointChargeSet(int point, String method) {
        paymentDto.setPayCount(point);
        paymentDto.setPaySort("충전");
        paymentDto.setPayType(method);
        paymentDto.setChargeCheck(true);
    }

    public void pointChargeSave() {
        if(paymentDto.chargeCheck==true) {
            System.out.println(paymentDto.getPayCount());
            System.out.println(paymentDto.getPaySort());
            Payment payment = Payment.toPayment(paymentDto);
            paymentRepository.save(payment);
            paymentDto.setChargeCheck(false);
        }else{
            System.out.println("중복 충전 방지");
        }
    }

    public void pointReturnSave(int point) {
        paymentDto.setPayCount(point);
        paymentDto.setPaySort("환급");
        paymentDto.setPayType("-");
        Payment payment = Payment.toPayment(paymentDto);
        paymentRepository.save(payment);
        System.out.println("환급 완료");
    }

    public void bookPaymentSave(int point) {
        paymentDto.setPayCount(point);
        paymentDto.setPaySort("결제");
        paymentDto.setPayType("엽전");
        Payment payment = Payment.toPayment(paymentDto);
        paymentRepository.save(payment);
        System.out.println("상품 결제 완료");
    }

    public List<Payment> findAll() {
        //List<Payment> list = paymentRepository.findAll();
        //List<PaymentDto> resultList = Arrays.asList((PaymentDto) list);
        return paymentRepository.findAll();
    }

    public List<Payment> findByLast() {
        return paymentRepository.findFirstByOrderByPayIdDesc();
    }

    public String kakaopayReady() {
        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "be1292fe91dbda03131d7b87113001ce");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME"); //가맹점 코드
        params.add("partner_order_id", "1001"); //가맹점 주문번호
        params.add("partner_user_id", "gorany"); //가맹점 회원 id
        params.add("item_name", "엽전 충전"); //상품명
        params.add("quantity", String.valueOf(paymentDto.getPayCount())); //상품 수량
        params.add("total_amount", String.valueOf(paymentDto.getPayCount()*100)); //상품 총액
        params.add("tax_free_amount", String.valueOf(paymentDto.getPayCount()*100)); //상품 비과세 금액
        params.add("approval_url", "http://localhost:8200/mypage/chargeResult");
        params.add("cancel_url", "http://localhost:8200/mypage/pointmanage");
        params.add("fail_url", "http://localhost:8200/mypage/pointmanage");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            kakaopayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaopayReadyVO.class);

            log.info("" + kakaopayReadyVO);
            System.out.println("url= "+kakaopayReadyVO.getNext_redirect_pc_url());

            return kakaopayReadyVO.getNext_redirect_pc_url();

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return kakaopayReadyVO.getNext_redirect_pc_url();
    }

    public KakaopayApprovalVO kakaopayInfo(String pg_token) {

        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "be1292fe91dbda03131d7b87113001ce");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaopayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "gorany");
        params.add("pg_token", pg_token);
        params.add("total_amount", String.valueOf(paymentDto.getPayCount()*100));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            kakaopayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaopayApprovalVO.class);
            log.info("" + kakaopayApprovalVO);

            return kakaopayApprovalVO;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


}
