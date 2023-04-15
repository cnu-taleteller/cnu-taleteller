package com.cnu.taleteller.backend.tool.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LayerId implements Serializable {

    private Long layer_id;
    private Long page_id;
}