package com.cnu.taleteller.backend.service;

import com.cnu.taleteller.backend.Repository.UserRepository;
import com.cnu.taleteller.backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAllDesc();
    }
}