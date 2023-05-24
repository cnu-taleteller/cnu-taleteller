package com.cnu.taleteller.backend.domain.tool.entity.mongo;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Page {
    private int pageId;

    private int pageStatus;

    private Caption caption;

    private String thumbnail;

    private Layer[] layerList;
}
