package com.example.server.domain.comment.entity;

import com.example.server.domain.post.entity.Post;
import com.example.server.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        // given
        int userId = 1;
        int postId = 2;
        User user = User.withId(userId);
        Post post = Post.withId(postId);
        String content = "content";

        // when
        Comment comment = Comment.create(user, post, content);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getUser()).isEqualTo(user);
        assertThat(comment.getPost()).isEqualTo(post);
    }
}