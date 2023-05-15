package com.cnu.taleteller.backend.domain.tool.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "tool")
@Getter
@Setter
@NoArgsConstructor
public class Tool {

    @Id
    private String id;

    private String name;
    private int age;
}