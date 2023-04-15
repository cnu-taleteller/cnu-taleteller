package com.cnu.taleteller.backend.domain.user.dto;

import com.cnu.taleteller.backend.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private int id;
    private String email;
    private String name;
    private Timestamp created_at;

    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.created_at = entity.getCreated_at();
    }
}