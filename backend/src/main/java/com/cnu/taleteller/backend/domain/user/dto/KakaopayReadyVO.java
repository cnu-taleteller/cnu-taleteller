package com.cnu.taleteller.backend.domain.user.dto;

import java.util.Date;
import lombok.Data;

@Data
public class KakaopayReadyVO {

    //response
    private String tid, next_redirect_pc_url;
    private Date created_at;
}
