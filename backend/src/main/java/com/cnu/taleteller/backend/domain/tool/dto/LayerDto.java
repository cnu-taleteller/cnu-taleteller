package com.cnu.taleteller.backend.domain.tool.dto;

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

    private Long pageId;

    private int layerX;

    private int layerY;

    private int layerXSize;

    private int layerYSize;

    private String fileName;

    private String fileSize;

    private String fileLocation;

    private String fileOriginName;

    public Layer toEntity() {
        return Layer.builder()
                .layerId(this.layerId)
                .pageId(Page.builder().pageId(this.pageId).build())
                .layerX(this.layerX)
                .layerY(this.layerY)
                .layerXSize(this.layerXSize)
                .layerYSize(this.layerYSize)
                .fileName(this.fileName)
                .fileSize(this.fileSize)
                .fileLocation(this.fileLocation)
                .fileOriginName(this.fileOriginName)
                .build();
    }

}