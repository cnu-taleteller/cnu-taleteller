package com.cnu.taleteller.backend.tool.dto;

import com.cnu.taleteller.backend.tool.domain.Layer;
import com.cnu.taleteller.backend.tool.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LayerDto {

    private Long layer_id;

    private Long page_id;

    private int layer_x;

    private int layer_y;

    private int layer_x_size;

    private int layer_y_size;

    private String file_name;

    private String file_size;

    private String file_location;

    private String file_origin_name;

    public Layer toEntity() {
        return Layer.builder()
                .layer_id(this.layer_id)
                .page_id(Page.builder().page_id(this.page_id).build())
                .layer_x(this.layer_x)
                .layer_y(this.layer_y)
                .layer_x_size(this.layer_x_size)
                .layer_y_size(this.layer_y_size)
                .file_name(this.file_name)
                .file_size(this.file_size)
                .file_location(this.file_location)
                .file_origin_name(this.file_origin_name)
                .build();
    }

}