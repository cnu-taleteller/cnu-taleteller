package com.cnu.taleteller.backend.domain.user.service;

import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.dto.MailDTO;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

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
    private String readHtmlTemplate(String templatePath) {
        try {
            Resource resource = new ClassPathResource("templates/" + templatePath);
            File file = resource.getFile();
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "HTML 템플릿 파일 읽기 실패";
        }
    }

    @Autowired
    private Environment env;
    public void mailSend(MailDTO mailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(mailDTO.getAddress());
            helper.setSubject(mailDTO.getTitle());


            String htmlContent = readHtmlTemplate("index.html");
            htmlContent = htmlContent.replace("{str}", mailDTO.getStr());

            helper.setText(htmlContent, true);

            String senderAddress = env.getProperty("spring.mail.username") + "@naver.com";
            helper.setFrom(senderAddress);
            helper.setReplyTo(senderAddress);

            System.out.println("message: " + message);

            mailSender.send(message);
            System.out.println("전송 완료!");
        } catch (MessagingException e) {
            System.out.println("전송 실패: " + e.getMessage());
        }
    }


    public MailDTO createMailAndChangePassword(String memberEmail) {
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(memberEmail);
        dto.setTitle("TaleTeller(이야기꾼) 임시비밀번호 안내 이메일 입니다.");
        dto.setStr(str);
        updatePasswordByEmail(str,memberEmail);
        return dto;
    }

    //임시 비밀번호로 업데이트
    public void updatePasswordByEmail(String newPassword, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + memberEmail));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String EncodePassword = passwordEncoder.encode(newPassword);
        member.setPassword(EncodePassword);
        memberRepository.save(member);
    }


    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
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


}
