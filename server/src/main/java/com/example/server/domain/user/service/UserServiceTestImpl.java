package com.example.server.domain.user.service;

import com.example.server.domain.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceTestImpl implements UserService {
    @Override
    public User getById(Integer userId) {
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");
        return user;
    }
}
