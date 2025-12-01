package com.example.server.domain.comment.service;

import com.example.server.domain.comment.dto.response.CommentResponse;
import com.example.server.domain.comment.entity.Comment;
import com.example.server.domain.comment.exception.CommentNotFoundException;
import com.example.server.domain.comment.repository.CommentRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("게시글에 댓글을 작성할 수 있다.")
    void canCreateComment() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        String content = "content";

        // when
        CommentResponse response = commentService.createComment(post.getId(), user.getId(), content);

        // then
        assertThat(response).isNotNull();
        assertThat(response.postId()).isEqualTo(post.getId());
        assertThat(response.user().userId()).isEqualTo(user.getId());
        assertThat(response.content()).isEqualTo(content);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();

        // 실제 DB에 저장되었는지 확인
        Comment savedComment = commentRepository.findById(response.id())
                .orElseThrow(() -> new AssertionError("댓글이 저장되지 않음"));
        assertThat(savedComment.getContent()).isEqualTo(content);
    }

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

    @Test
    @DisplayName("존재하지 않는 postId로 댓글 작성 시 PostNotFoundException을 던진다.")
    void throwsPostNotFoundExceptionWhenPostDoesNotExist() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        int invalidPostId = 999;
        String content = "content";

        // when then
        assertThatThrownBy(() -> commentService.createComment(invalidPostId, user.getId(), content))
                .isInstanceOf(PostNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하지 않는 userId로 댓글 작성 시 UserNotFoundException을 던진다.")
    void throwsUserNotFoundExceptionWhenUserDoesNotExist() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        int invalidUserId = 999;
        String content = "content";

        // when then
        assertThatThrownBy(() -> commentService.createComment(post.getId(), invalidUserId, content))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("commentId에 해당하는 Comment가 없을 경우 CommentNotFoundException을 던진다.")
    void throwsCommentNotFoundExceptionWhenCommentWithCommentIdDoesNotExist() {
        // given when then
        assertThatThrownBy(() -> commentService.getById(1))
                .isInstanceOf(CommentNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("postId로 Comment 목록을 페이지 단위로 조회할 수 있다.")
    void canGetCommentByPostId() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        // 복수의 댓글 생성
        for (int i = 1; i <= 5; i++) {
            Comment comment = Comment.create(user, post, "content " + i);
            commentRepository.save(comment);
        }

        int pageSize = 3;
        Pageable pageable = PageRequest.of(0, pageSize);

        // when
        Slice<CommentResponse> slice = commentService.getByPostId(post.getId(), pageable);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getContent()).hasSize(pageSize); // 페이지 크기만큼 조회
        assertThat(slice.getContent().getFirst().postId()).isEqualTo(post.getId());
        assertThat(slice.hasNext()).isTrue(); // 전체 5개 중 3개 조회, 다음 페이지 있음
    }
}