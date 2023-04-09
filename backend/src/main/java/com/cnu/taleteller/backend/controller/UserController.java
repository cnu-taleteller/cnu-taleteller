package com.cnu.taleteller.backend.controller;


import com.cnu.taleteller.backend.domain.User;
import com.cnu.taleteller.backend.dto.UserResponseDto;
import com.cnu.taleteller.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/test")
    public String test(@RequestBody Map<String, String> params){
        System.out.println("vue 통신 성공 "+params.get("data"));
        return "ok";
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