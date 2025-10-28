package com.example.server.domain.user.service;

import com.example.server.domain.user.domain.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
    }
}
