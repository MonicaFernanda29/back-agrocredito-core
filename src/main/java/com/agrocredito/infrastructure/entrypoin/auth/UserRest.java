package com.agrocredito.infrastructure.entrypoin.auth;

import com.agrocredito.domain.model.repository.user.UserRepository;
import com.agrocredito.infrastructure.entrypoin.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserRest {

    private final UserRepository userRepository;

    @GetMapping("list")
    public List<UserResponse> changePassword() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder().email(user.getEmail()).name(user.getName()).build())
                .toList();
    }
}
