package com.example.server.domain.user.service;

import com.example.server.domain.user.entity.User;

public interface UserService {
    User getById(Integer userId);
    void validateUsername(String username);
    void changeUsername(Integer userId, String username);
}
