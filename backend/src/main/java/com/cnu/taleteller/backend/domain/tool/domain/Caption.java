package com.cnu.taleteller.backend.domain.tool.domain;


import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Caption {
    private String fontSize;
    private String fontColor;
    private String content;
    private String height;
    private String width;
    private String left;
    private String top;
}
