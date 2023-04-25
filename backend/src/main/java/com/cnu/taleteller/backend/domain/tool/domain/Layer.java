package com.cnu.taleteller.backend.domain.tool.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "layers")
@NoArgsConstructor
@IdClass(LayerId.class)
public class Layer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long layerId;

    @Id
    @ManyToOne
    @JoinColumn(name = "pageIdx")
    private Page pageIdx;

    @Column(name = "layer_x")
    private int layerX;

    @Column(name = "layer_y")
    private int layerY;

    @Column(name = "layer_x_size")
    private int layerXSize;

    @Column(name = "layer_y_size")
    private int layerYSize;

    @Column(length = 101)
    private String fileName;

    @Column(length = 10)
    private String fileSize;

    @Column(length = 100)
    private String fileOriginName;

    private int layerNumber;

    @OneToOne
    @JoinColumn(name = "fileId")
    private File fileId;

    @Builder
    public Layer(Long layerId, Page pageIdx, int layerX, int layerY, int layerXSize, int layerYSize, String fileName, String fileSize, String fileOriginName, int layerNumber, File fileId) {
        this.layerId = layerId;
        this.pageIdx = pageIdx;
        this.layerX = layerX;
        this.layerY = layerY;
        this.layerXSize = layerXSize;
        this.layerYSize = layerYSize;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileOriginName = fileOriginName;
        this.layerNumber = layerNumber;
        this.fileId = fileId;
    }
}