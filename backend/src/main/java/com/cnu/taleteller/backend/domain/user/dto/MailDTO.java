package com.cnu.taleteller.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {
    private String address;
    private String senderAddress;
    private String title;
    private String message;
    private String str;
}
