package com.cnu.taleteller.backend.domain.user.dto;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberManagementDto {
    private String memberAuth;

    @Builder
    public MemberManagementDto(String memberAuth) {
        this.memberAuth = memberAuth;
    }

    public Member toEntity(){
        return Member.builder().memberAuth(memberAuth).build();
    }
}