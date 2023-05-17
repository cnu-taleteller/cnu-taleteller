package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException((memberEmail)));
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
