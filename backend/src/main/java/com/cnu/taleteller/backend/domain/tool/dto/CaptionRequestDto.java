package com.cnu.taleteller.backend.domain.tool.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CaptionRequestDto {

    private String story;

    private List<String> captions;
}
