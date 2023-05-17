package com.cnu.taleteller.backend.domain.tool.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class KeywordRequestDto {

    private String who;

    private String when;

    private String where;

    private String event;

}
