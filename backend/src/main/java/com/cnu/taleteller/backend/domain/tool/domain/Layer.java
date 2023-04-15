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
    private Long layer_id;

    @Id
    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page_id;

    private int layer_x;

    private int layer_y;

    private int layer_x_size;

    private int layer_y_size;

    @Column(length = 101)
    private String file_name;

    @Column(length = 10)
    private String file_size;

    @Column(length = 100)
    private String file_location;

    @Column(length = 100)
    private String file_origin_name;
}