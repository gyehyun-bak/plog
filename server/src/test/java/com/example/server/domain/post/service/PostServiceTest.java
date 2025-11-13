package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.request.PostRequest;
import com.example.server.domain.post.dto.response.PostResponse;
import com.example.server.domain.post.entity.Post;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.post.repository.PostRepository;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.exception.UserNotFoundException;
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
    @DisplayName("postId에 해당하는 Post가 존재하지 않을 경우 PostNotFoundException이 발생한다.")
    void throwPostNotFoundExceptionWhenPostWithPostIdDoesntExists() {
        // given
        int invalidPostId = 9999;

        // when then
        assertThatThrownBy(() -> postService.getById(invalidPostId))
                .isInstanceOf(PostNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @Test
    @DisplayName("생성하고자 하는 User의 id와 PostRequest로 새로운 Post를 생성할 수 있다.")
    void canCreateNewPostWithUserIdAndPostRequest() {
        // given
        User author = User.create("username", "test@email.com", "TEST", "test-id");
        userRepository.save(author);

        String title = "title";
        String content = "content";

        PostRequest request = new PostRequest(title, content);

        // when
        PostResponse saved = postService.createPost(author.getId(), request);

        // then
        assertThat(saved.postId()).isNotNull();
        assertThat(saved.author().userId()).isEqualTo(author.getId());
        assertThat(saved.author().username()).isEqualTo(author.getUsername());
        assertThat(saved.title()).isEqualTo(title);
        assertThat(saved.content()).isEqualTo(content);
        assertThat(saved.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("authorId에 해당하는 User가 존재하지 않을 경우 UserNotFoundException이 발생한다.")
    void throwUserNotFoundExceptionIfUserWithAuthorIdDoesntExists() {
        // given
        int invalidUserId = 9999;
        PostRequest request = new PostRequest("title", "content");

        // when then
        assertThatThrownBy(() -> postService.createPost(invalidUserId, request))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }
}