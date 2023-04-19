package com.cnu.taleteller.backend.domain.tool.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "layers")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(LayerId.class)
public class Layer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long layerId;

    @Id
    @ManyToOne
    @JoinColumn(name = "pageId")
    private Page pageId;

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

}