package com.example.server.domain.user.dto.response;

import com.example.server.domain.user.entity.User;

public record UserResponse(
        int userId,
        String username
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
