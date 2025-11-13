package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.response.PostResponse;
import com.example.server.domain.post.entity.Post;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.post.repository.PostRepository;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    @DisplayName("postId로 Post를 조회할 수 있다.")
    void canGetPostById() {
        // given
        User author = User.create("username", "test@email.com", "TEST", "test-id");
        userRepository.save(author);
        Post post = Post.create(author, "title", "content");
        postRepository.save(post);

        // when
        PostResponse found = postService.getById(post.getPostId());

        // then
        assertThat(found.postId()).isEqualTo(post.getPostId());
        assertThat(found.author().userId()).isEqualTo(author.getId());
        assertThat(found.author().username()).isEqualTo(author.getUsername());
        assertThat(found.title()).isEqualTo(post.getTitle());
        assertThat(found.content()).isEqualTo(post.getContent());
        assertThat(found.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 postId로 조회시 PostNotFoundException을 던진다.")
    void throwPostNotFoundExceptionWhenPostWithPostIdDoesntExists() {
        // given
        int invalidPostId = 9999;

        // when then
        assertThatThrownBy(() -> postService.getById(invalidPostId))
                .isInstanceOf(PostNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }
}