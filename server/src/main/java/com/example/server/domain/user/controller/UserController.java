package com.example.server.domain.user.controller;

import com.example.server.domain.user.dto.response.UserResponse;
import com.example.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{publicId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String publicId) {
        return ResponseEntity.ok(userService.getById(publicId));
    }
}
