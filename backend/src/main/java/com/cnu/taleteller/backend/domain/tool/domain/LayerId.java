package com.cnu.taleteller.backend.domain.tool.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LayerId implements Serializable {

    private Long layerId;
    private Long pageIdx;
}