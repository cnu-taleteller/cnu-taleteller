package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.tool.domain.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDto {

    private Long fileId;

    private String fileName;

    private String fileSize;

    public File toEntity(){
        return File.builder()
                .fileId(this.fileId)
                .fileName(this.fileName)
                .fileSize(this.fileSize)
                .build();
    }
}
