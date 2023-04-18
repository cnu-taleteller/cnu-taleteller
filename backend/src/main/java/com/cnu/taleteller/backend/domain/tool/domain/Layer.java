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

    private int layerX;

    private int layerY;

    private int layerXSize;

    private int layerYSize;

    @Column(length = 101)
    private String fileName;

    @Column(length = 10)
    private String fileSize;

    @Column(length = 100)
    private String fileLocation;

    @Column(length = 100)
    private String fileOriginName;
}