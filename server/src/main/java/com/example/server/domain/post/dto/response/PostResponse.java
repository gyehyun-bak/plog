package com.example.server.domain.post.dto.response;

import com.example.server.domain.post.entity.Post;
import com.example.server.domain.user.dto.response.UserResponse;

import java.time.LocalDateTime;

public record PostResponse(
        int postId,
        UserResponse author,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static PostResponse fromEntity(Post post) {
        return new PostResponse(
                post.getPostId(),
                UserResponse.fromEntity(post.getAuthor()),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }
}
