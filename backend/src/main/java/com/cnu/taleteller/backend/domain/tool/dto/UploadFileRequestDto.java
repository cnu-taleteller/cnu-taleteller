package com.cnu.taleteller.backend.domain.tool.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UploadFileRequestDto {

    private List<String> uploadCharList;
    private List<String> uploadBackList;

}