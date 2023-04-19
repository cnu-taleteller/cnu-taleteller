package com.cnu.taleteller.backend.domain.tool.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long fileId;

    @Column(length = 100)
    private String fileName;

    @Column(length = 10)
    private String fileSize;
}
