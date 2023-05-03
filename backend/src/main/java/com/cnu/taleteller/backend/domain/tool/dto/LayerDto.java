package com.cnu.taleteller.backend.domain.tool.dto;

import com.cnu.taleteller.backend.domain.tool.domain.File;
import com.cnu.taleteller.backend.domain.tool.domain.Layer;
import com.cnu.taleteller.backend.domain.tool.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayerDto {

    private Long layerId;

    private Long pageIdx;

    private int layerX;

    private int layerY;

    private int layerXSize;

    private int layerYSize;

    private String fileName;

    private String fileSize;

    private String fileOriginName;

    private int layerNumber;

    private Long fileId;

    public Layer toEntity() {
        return Layer.builder()
                .layerId(this.layerId)
                .pageIdx(Page.builder().pageIdx(this.pageIdx).build())
                .layerX(this.layerX)
                .layerY(this.layerY)
                .layerXSize(this.layerXSize)
                .layerYSize(this.layerYSize)
                .fileName(this.fileName)
                .fileSize(this.fileSize)
                .fileOriginName(this.fileOriginName)
                .layerNumber(this.layerNumber)
                .fileId(File.builder().fileId(this.fileId).build())
                .build();
    }

}