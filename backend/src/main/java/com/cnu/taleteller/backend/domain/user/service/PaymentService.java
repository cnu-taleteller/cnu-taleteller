package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.Repository.PaymentRepository;
import com.cnu.taleteller.backend.domain.user.dto.PaymentBookDto;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.entity.Payment;
import com.cnu.taleteller.backend.domain.user.dto.KakaopayApprovalVO;
import com.cnu.taleteller.backend.domain.user.dto.KakaopayReadyVO;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
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
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    PaymentDto paymentDto = new PaymentDto();

    private static final String HOST = "https://kapi.kakao.com";
    private KakaopayReadyVO kakaopayReadyVO;
    private KakaopayApprovalVO kakaopayApprovalVO;

    @Value("${SITE_PATH}")
    private String siteUri;

    public void pointChargeSet(int point, String method, String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        paymentDto.setPayCount(point);
        paymentDto.setPaySort("충전");
        paymentDto.setPayType(method);
        paymentDto.setMemberId(memId);
        paymentDto.setChargeCheck(true);

    }

    public void pointChargeSave() {
        if(paymentDto.chargeCheck==true) {
            System.out.println(paymentDto.getPaySort()+" : "+paymentDto.getPayCount()+"개");

            //Dto에 저장된 id값으로 Member 객체를 직업 찾아 넣음
            Member member = memberRepository.findById(paymentDto.getMemberId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + paymentDto.getMemberId()));

            paymentRepository.save(paymentDto.toEntity(member)).getPayId();
            paymentDto.setChargeCheck(false);
        }else{
            System.out.println("중복 충전 방지");
        }
    }

    public void pointReturnSave(int point, String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        paymentDto.setPayCount(point);
        paymentDto.setPaySort("환급");
        paymentDto.setPayType("-");
        paymentDto.setMemberId(memId);
        
        Member member = memberRepository.findById(paymentDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + paymentDto.getMemberId()));

        paymentRepository.save(paymentDto.toEntity(member)).getPayId();
        System.out.println("환급 완료");
    }

    public Payment bookPaymentSave(Long bookId, PaymentBookDto paymentBookDto) {
        Member member = memberRepository.findByMemberEmail(paymentBookDto.getMemberEmail()).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

//      paymentRepository.findByMemberAndBook(member, book);
        Payment payment;

        paymentBookDto.setPayCount(-20);
        paymentBookDto.setPaySort("결제");
        paymentBookDto.setPayType("엽전");
        paymentBookDto.setMemberId(member);
        paymentBookDto.setBookId(book);
        payment = paymentRepository.save(paymentBookDto.toEntity());
        System.out.println("상품 결제 완료");

        return payment;
    }

    public List<Payment> findAllMyPayList(String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();

        return paymentRepository.findAllMyPayList(memId);
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
        params.add("approval_url", siteUri+"/mypage/chargeResult");
        params.add("cancel_url", siteUri+"/mypage/pointmanage");
        params.add("fail_url", siteUri+"/mypage/pointmanage");

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
