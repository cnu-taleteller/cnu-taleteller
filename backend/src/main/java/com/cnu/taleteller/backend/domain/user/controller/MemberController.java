package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.user.dto.MailDTO;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.dto.MemberInfoDto;
import com.cnu.taleteller.backend.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final UserDetailsService memberDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:8200/")  // CORS정책 해당 출처 허용
    public ResponseEntity<MemberInfoDto> signup(@RequestBody MemberInfoDto infoDto) { // 회원 추가
        memberService.save(infoDto);
        return null;
    }


    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:8200/")
    public Boolean login(@RequestBody Map<String, String> loginInfo, HttpSession session) {
        String memberEmail = loginInfo.get("memberEmail");
        String memberPassword = loginInfo.get("memberPassword");

        try {
            Member member = memberService.loadUserByUsername(memberEmail);
            if (!passwordEncoder.matches(memberPassword, member.getMemberPassword())) {
                throw new UsernameNotFoundException(memberEmail);
            }
            session.setAttribute("user", memberEmail);
            return memberService.loginDropCheck(memberEmail);
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @PostMapping("/checkemail")
    @CrossOrigin(origins = "http://localhost:8200/")  // 해당 출처 허용
    public Boolean checkemail(@RequestBody Map<String, String> loginInfo) {
        String memberEmail = loginInfo.get("memberEmail");
        try {
            UserDetails member = memberDetailsService.loadUserByUsername(memberEmail);
            return true;
        } catch (UsernameNotFoundException e) {
            return false; // 해당 이메일을 가진 회원이 존재하지 않음
        }
    }

    @Transactional
    @PostMapping("/sendEmail")
    @CrossOrigin(origins = "http://localhost:8200/")  // 해당 출처 허용
    public String sendEmail(@RequestParam("memberEmail") String memberEmail){
        try {
            MailDTO dto = memberService.createMailAndChangePassword(memberEmail);
            memberService.mailSend(dto);
            return "yes";
        } catch (Exception e) {
            System.out.println("입력 실패: " + e.getMessage());
            return "no";
        }
    }




    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/LoginView";
    }

    @GetMapping("/pwcheck")
    public Boolean checkpsword(@RequestParam String memberEmail, @RequestParam String memberPassword) {
        return memberService.findPassword(memberEmail, memberPassword);
    }

    @GetMapping("/details")
    public ResponseEntity<List<Member>> memberDetails(@RequestParam String memberEmail) {

        List<Member> memberDetails = memberService.findAllByMemberEmail(memberEmail);
        return ResponseEntity.ok(memberDetails);
    }

    @PatchMapping("/dropout/{email}")
    public String memberDropout(@PathVariable String email, @RequestBody MemberInfoDto params) {
        return memberService.memDrop(email, params);
    }
    @PatchMapping("/modify/{email}")
    public String memberModify(@PathVariable String email, @RequestBody MemberInfoDto params) {
        return memberService.memModify(email, params);
    }
}
