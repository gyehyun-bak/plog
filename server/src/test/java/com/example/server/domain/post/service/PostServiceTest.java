package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.request.PostRequest;
import com.example.server.domain.post.dto.response.PostResponse;
import com.example.server.domain.post.entity.Post;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.post.exception.UserNotAllowedUpdatePostException;
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

    @Test
    @DisplayName("userId와 postId, PostRequest로 기존 Post를 수정할 수 있다.")
    void canUpdatePostWithUserIdPostIdAndPostRequest() {
        // given
        User author = User.create("author", "author@email.com", "Author", "author-id");
        userRepository.save(author);
        Post post = Post.create(author, "original title", "original content");
        postRepository.save(post);

        String newTitle = "updated title";
        String newContent = "updated content";
        PostRequest request = new PostRequest(newTitle, newContent);

        // when
        PostResponse updated = postService.updatePost(author.getId(), post.getPostId(), request);

        // then
        assertThat(updated.postId()).isEqualTo(post.getPostId());
        assertThat(updated.author().userId()).isEqualTo(author.getId());
        assertThat(updated.title()).isEqualTo(newTitle);
        assertThat(updated.content()).isEqualTo(newContent);
        assertThat(updated.updatedAt()).isNotNull();
    }

    @Test
    @DisplayName("수정을 요청하는 사용자가 존재하지 않는 경우 UserNotFoundException이 발생한다.")
    void throwUserNotFoundExceptionIfUserTryingToUpdatePostDoesntExists() {
        // given
        User author = User.create("author", "author@email.com", "Author", "author-id");
        userRepository.save(author);
        Post post = Post.create(author, "original title", "original content");
        postRepository.save(post);
        PostRequest request = new PostRequest("newTitle", "newContent");

        int invalidUserId = author.getId() + 9999;

        // when then
        assertThatThrownBy(() -> postService.updatePost(invalidUserId, post.getPostId(), request))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하지 않는 postId로 수정 요청 시 PostNotFoundException이 발생한다.")
    void throwPostNotFoundExceptionWhenPostIdDoesNotExistForUpdate() {
        // given
        User author = User.create("author", "author@email.com", "Author", "author-id");
        userRepository.save(author);

        int invalidPostId = 9999;
        PostRequest updateRequest = new PostRequest("title", "content");

        // when then
        assertThatThrownBy(() -> postService.updatePost(author.getId(), invalidPostId, updateRequest))
                .isInstanceOf(PostNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @Test
    @DisplayName("권한이 없는 user가 Post를 수정할 경우 UserNotAllowedUpdatePostException이 발생한다.")
    void throwUserNotAllowedUpdatePostExceptionWhenUnauthorizedUserTriesToUpdatePost() {
        // given
        User author = User.create("author", "author@email.com", "Author", "author-id");
        userRepository.save(author);
        Post post = Post.create(author, "title", "content");
        postRepository.save(post);

        User otherUser = User.create("other", "other@email.com", "Other", "other-id");
        userRepository.save(otherUser);

        PostRequest updateRequest = new PostRequest("new title", "new content");

        // when then
        assertThatThrownBy(() -> postService.updatePost(otherUser.getId(), post.getPostId(), updateRequest))
                .isInstanceOf(UserNotAllowedUpdatePostException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_ALLOWED_UPDATE_POST);
    }
}