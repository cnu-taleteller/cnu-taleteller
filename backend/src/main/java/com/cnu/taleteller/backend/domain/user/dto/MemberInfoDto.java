package com.cnu.taleteller.backend.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MemberInfoDto {
//    private int id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberAuth;
    private String memberPhone;
    private String memberAccount;
}