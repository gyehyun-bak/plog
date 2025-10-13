package com.example.server.domain.user.service;

import com.example.server.domain.user.domain.User;

public interface UserService {
    User getById(Integer userId);
}
