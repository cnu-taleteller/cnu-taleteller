package com.cnu.taleteller.backend.domain.user.controller;


import com.cnu.taleteller.backend.domain.user.entity.User;
import com.cnu.taleteller.backend.domain.user.dto.UserResponseDto;
import com.cnu.taleteller.backend.domain.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userService.findAllUsers();
        List<UserResponseDto> userResponseDtoList = userList.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return userResponseDtoList;
    }
}