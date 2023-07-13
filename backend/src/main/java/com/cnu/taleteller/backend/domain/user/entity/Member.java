package com.cnu.taleteller.backend.domain.user.entity;

import com.cnu.taleteller.backend.domain.user.dto.MemberInfoDto;
import com.cnu.taleteller.backend.domain.user.dto.PaymentDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name="members")
@Getter
@Setter
@DynamicInsert
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "member_email", unique = true, nullable = false)
    private String memberEmail;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @ColumnDefault("'User'")
    private String memberAuth;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_phone", nullable = false)
    private String memberPhone;
    @Column(name = "member_account", nullable = false)
    private String memberAccount;

    public static Member toMember(MemberInfoDto memberInfoDto) {
        Member member = new Member();
        member.setMemberId(memberInfoDto.getMemberId());
        member.setMemberAuth(memberInfoDto.getMemberAuth());
        member.setMemberPassword(memberInfoDto.getMemberPassword());
        member.setMemberPhone(memberInfoDto.getMemberPhone());
        member.setMemberAccount(memberInfoDto.getMemberAccount());
        return member;
    }

    @Builder
    public Member(String memberEmail, String memberPassword, String memberAuth, String memberName, String memberPhone, String memberAccount) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.memberAuth = memberAuth;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.memberAccount = memberAccount;
    }

    public void dropUpdate(String memberAuth) {
        this.memberAuth = memberAuth;
    }

    public void modifyUpdate(String memberPassword, String memberPhone, String memberAccount) {
        this.memberPassword = memberPassword;
        this.memberPhone = memberPhone;
        this.memberAccount = memberAccount;
    }

    public void modifyAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    // 사용자의 권한을 콜렉션 형태로 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : memberAuth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    public void setPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }
    @Override
    public String getUsername() {
        return memberEmail;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return memberPassword;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }

    public void suspend(Long memberId, String memberAuth) {
        this.memberId = memberId;
        this.memberAuth = memberAuth;
    }
}