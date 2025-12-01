package com.example.server.domain.comment.entity;

import com.example.server.domain.post.entity.Post;
import com.example.server.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        // given
        int userId = 1;
        int postId = 2;
        User user = User.withId(userId);
        Post

        // when

        // then
        Comment comment = new Comment();
    }
}