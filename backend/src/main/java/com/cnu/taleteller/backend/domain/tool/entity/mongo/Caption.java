package com.cnu.taleteller.backend.domain.tool.entity.mongo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Caption {
    private int captionState;
    private String fontSize;
    private String fontColor;
    private String content;
    private String height;
    private String width;
    private String left;
    private String top;
}
