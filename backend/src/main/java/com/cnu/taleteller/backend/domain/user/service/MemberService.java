package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.domain.Member;
import com.cnu.taleteller.backend.domain.user.domain.Payment;
import com.cnu.taleteller.backend.domain.user.dto.MemberInfoDto;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException((memberEmail)));
    }

    public Boolean loginDropCheck(String email) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        if(findMember.getMemberAuth().equals("Drop")){
            System.out.println("로그인 시도 - 탈퇴한 회원 이메일");
            return false; //memberAuth값이 Drop면 false
        }
        System.out.println("로그인 시도 - 정상 이메일 확인");
        return true;
    }

    public Long save(MemberInfoDto infoDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setMemberPassword(encoder.encode(infoDto.getMemberPassword()));

        return memberRepository.save(Member.builder()
                .memberEmail(infoDto.getMemberEmail())
                .memberAuth(infoDto.getMemberAuth())
                .memberPassword(infoDto.getMemberPassword())
                .memberName(infoDto.getMemberName())
                .memberPhone(infoDto.getMemberPhone())
                .memberAccount(infoDto.getMemberAccount()).build()).getMemberId();
    }

    public boolean findPassword (String email, String psword ) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        System.out.println("현재이메일 : "+email+"\n입력패스워드 : "+psword);

        if(findMember == null){
            System.out.println("데이터 null");
            return false; //데이터 없으면 false
        }
        if(!findMember.getMemberPassword().equals(psword)){
            System.out.println("실제패스워드 : "+findMember.getMemberPassword());
            System.out.println("비번 불일치(비번 복호화 필요)");
            return true; //해당 데이터가 입력한 비밀번호와 불일치 시 false
        }
        System.out.println("비번 일치");
        return true; //null이 아니거나 비밀번호 일치 시 true
    }

    public List<Member> findAllByMemberEmail(String email) {
        return memberRepository.findAllByMemberEmail(email);
    }

    @Transactional
    public String memDrop(String email, MemberInfoDto params) {
        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();
        Member entity = memberRepository.findById(memId).orElseThrow();
        entity.dropUpdate(params.getMemberAuth());
        return email;
    }

    @Transactional
    public String memModify(String email, MemberInfoDto params) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        params.setMemberPassword(encoder.encode(params.getMemberPassword()));

        Member findMember = memberRepository.findDistinctByMemberEmail(email);
        Long memId = findMember.getMemberId();
        Member entity = memberRepository.findById(memId).orElseThrow();
        entity.modifyUpdate(params.getMemberPassword(), params.getMemberPhone(), params.getMemberAccount());
        return email;
    }

//    public String login(MemberInfoDto memberDto) {
//        Member member = memberRepository.findByMemberEmail(memberDto.getMemberEmail());
//        if (member == null) {
//            return "FAIL";
//        } else {
//            if (member.getMemberPassword().equals(memberDto.getMemberPassword())) {
//                return "SUCCESS";
//            } else {
//                return "FAIL";
//            }
//        }
//    }
}
