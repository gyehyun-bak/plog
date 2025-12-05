package com.example.server.domain.user.dto.response;

import com.example.server.domain.user.entity.User;

public record UserResponse(
        String id,
        String username
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getPublicId(),
                user.getUsername()
        );
    }
}
