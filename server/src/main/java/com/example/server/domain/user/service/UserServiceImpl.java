package com.example.server.domain.user.service;

import com.example.server.domain.user.dto.response.UserResponse;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.exception.UserNotFoundException;
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

    @Transactional(readOnly = true)
    @Override
    public UserResponse getById(String publicId) {
        return UserResponse.fromEntity(userRepository.findByPublicId(publicId).orElseThrow(UserNotFoundException::new));
    }

    @Transactional(readOnly = true)
    @Override
    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BaseException(ErrorCode.USERNAME_TAKEN);
        }

        if (username == null || username.isBlank() ||
                username.length() > 30 ||
                !username.matches("^[a-zA-Z0-9._]+$") ||
                username.startsWith(".") || username.endsWith(".") ||
                username.contains("..")) {

            throw new BaseException(ErrorCode.INVALID_USERNAME);
        }
    }

    @Override
    public void changeUsername(int userId, String username) {
        validateUsername(username);

        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        user.changeUsername(username);
    }
}
