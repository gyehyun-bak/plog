package com.example.server.domain.comment.dto.response;

import com.example.server.domain.comment.entity.Comment;
import com.example.server.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

public record CommentResponse(
        UserResponse user,
        int postId,
        String content,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
                UserResponse.fromEntity(comment.getUser()),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getUpdatedAt(),
                comment.getCreatedAt()
        );
    }
}
