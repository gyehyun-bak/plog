package com.example.server.domain.comment.service;

import com.example.server.domain.comment.dto.response.CommentResponse;
import com.example.server.domain.comment.entity.Comment;
import com.example.server.domain.comment.repository.CommentRepository;
import com.example.server.domain.post.entity.Post;
import com.example.server.domain.post.repository.PostRepository;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("commentId로 Comment를 조회할 수 있다.")
    void canGetCommentById() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        String content = "content";
        Comment comment = Comment.create(user, post, content);
        commentRepository.save(comment);

        // when
        CommentResponse response = commentService.getById(comment.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.user().userId()).isEqualTo(user.getId());
        assertThat(response.postId()).isEqualTo(post.getId());
        assertThat(response.content()).isEqualTo(content);
        assertThat(response.updatedAt()).isNotNull();
        assertThat(response.createdAt()).isNotNull();
    }
}