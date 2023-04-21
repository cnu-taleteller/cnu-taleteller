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

    @PostMapping("/test")
    public String test(@RequestBody Map<String, String> params) {
        System.out.println("vue 통신 성공 " + params.get("data"));
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

    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addPhoto(@RequestParam("image") MultipartFile img) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + img.getOriginalFilename();
        String path = "D:/project/cnu-taleteller/frontend/public/images/";
        Path imagePath = Paths.get(path + imageFileName);
        try {
            Files.write(imagePath, img.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(img.getOriginalFilename());
        return imageFileName;
    }
}