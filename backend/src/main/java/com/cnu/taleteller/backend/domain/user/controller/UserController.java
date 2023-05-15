package com.cnu.taleteller.backend.domain.user.controller;


import com.cnu.taleteller.backend.domain.user.domain.User;
import com.cnu.taleteller.backend.domain.user.dto.UserResponseDto;
import com.cnu.taleteller.backend.domain.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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