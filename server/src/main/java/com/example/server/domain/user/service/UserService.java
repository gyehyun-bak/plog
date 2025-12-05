package com.example.server.domain.user.service;

import com.example.server.domain.user.dto.response.UserResponse;

public interface UserService {
    UserResponse getById(String publicId);
    void validateUsername(String username);
    void changeUsername(int userId, String username);
}
