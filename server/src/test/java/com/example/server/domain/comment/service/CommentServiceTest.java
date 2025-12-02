package com.example.server.domain.comment.service;

import com.example.server.AbstractIntegrationTest;
import com.example.server.domain.comment.dto.request.CommentRequest;
import com.example.server.domain.comment.dto.response.CommentResponse;
import com.example.server.domain.comment.entity.Comment;
import com.example.server.domain.comment.exception.CommentNotFoundException;
import com.example.server.domain.comment.exception.UserNotAllowedDeleteCommentException;
import com.example.server.domain.comment.exception.UserNotAllowedUpdateCommentException;
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
class CommentServiceTest extends AbstractIntegrationTest {

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
        CommentRequest request = new CommentRequest(content);

        // when
        CommentResponse response = commentService.createComment(post.getId(), user.getId(), request);

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
    @DisplayName("존재하지 않는 postId로 댓글 작성 시 PostNotFoundException을 던진다.")
    void throwsPostNotFoundExceptionWhenPostDoesNotExist() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        int invalidPostId = 999;
        String content = "content";
        CommentRequest request = new CommentRequest(content);

        // when then
        assertThatThrownBy(() -> commentService.createComment(invalidPostId, user.getId(), request))
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
        CommentRequest request = new CommentRequest(content);

        // when then
        assertThatThrownBy(() -> commentService.createComment(post.getId(), invalidUserId, request))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
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

    @Test
    @DisplayName("댓글을 작성한 User는 댓글을 수정할 수 있다.")
    void canUpdateComment() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        String content = "content";
        Comment comment = Comment.create(user, post, content);
        commentRepository.save(comment);

        // when
        String newContent = "newContent";
        CommentRequest request = new CommentRequest(newContent);
        CommentResponse response = commentService.updateComment(user.getId(), comment.getId(), request);

        // then
        assertThat(response.content()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("댓글의 작성자 아닌 User가 댓글 수정을 요청하면 UserNotAllowedUpdateCommentException을 던진다.")
    void throwsUserNotAllowedUpdateCommentExceptionWhenUserDidntWriteComment() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        Post post = Post.create(user, "testPost", "test-content");
        userRepository.save(user);
        postRepository.save(post);

        String content = "content";
        Comment comment = Comment.create(user, post, content);
        commentRepository.save(comment);

        // when then
        User otherUser = User.create("otherUser", "otherUser@email.com", "TEST", "test-id");
        userRepository.save(otherUser);

        CommentRequest request = new CommentRequest("newContent");
        assertThatThrownBy(() -> commentService.updateComment(otherUser.getId(), comment.getId(), request))
                .isInstanceOf(UserNotAllowedUpdateCommentException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_ALLOWED_UPDATE_COMMENT);
    }

    @Test
    @DisplayName("존재하지 않는 commentId로 댓글 수정을 요청하면 CommentNotFoundException을 던진다.")
    void throwsCommentNotFoundExceptionWhenCommentIdDoesNotExistOnUpdateComment() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when then
        assertThatThrownBy(() -> commentService.updateComment(user.getId(), -1, new CommentRequest("newContent")))
                .isInstanceOf(CommentNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("Comment 작성자는 Comment를 삭제할 수 있다.")
    void commentWriterCanDeleteComment() {
        // given
        User author = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(author);

        Post post = Post.create(author, "testPost", "test-content");
        postRepository.save(post);

        User commentUser = User.create("commentUser", "commentUser@email.com", "TEST", "test-id");
        userRepository.save(commentUser);

        String content = "content";
        Comment comment = Comment.create(commentUser, post, content);
        commentRepository.save(comment);

        // when
        commentService.deleteComment(commentUser.getId(), comment.getId());

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("Post 작성자는 댓글을 삭제할 수 있다.")
    void postAuthorCanDeleteComment() {
        // given
        User author = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(author);

        Post post = Post.create(author, "testPost", "test-content");
        postRepository.save(post);

        User commentUser = User.create("commentUser", "commentUser@email.com", "TEST", "test-id");
        userRepository.save(commentUser);

        String content = "content";
        Comment comment = Comment.create(commentUser, post, content);
        commentRepository.save(comment);

        // when
        commentService.deleteComment(author.getId(), comment.getId());

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("권한이 없는 User가 댓글 삭제를 요청할 경우 UserNotAllowedDeleteCommentException을 던진다.")
    void throwsUserNotAllowedDeleteCommentExceptionWhenUserNotAllowed() {
        // given
        User author = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(author);

        Post post = Post.create(author, "testPost", "test-content");
        postRepository.save(post);

        User commentUser = User.create("commentUser", "commentUser@email.com", "TEST", "test-id");
        userRepository.save(commentUser);

        String content = "content";
        Comment comment = Comment.create(commentUser, post, content);
        commentRepository.save(comment);

        // when then
        User otherUser = User.create("otherUser", "otherUser@email.com", "TEST", "test-id");
        userRepository.save(otherUser);
        assertThatThrownBy(() -> commentService.deleteComment(otherUser.getId(), comment.getId()))
                .isInstanceOf(UserNotAllowedDeleteCommentException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_ALLOWED_DELETE_COMMENT);
    }

    @Test
    @DisplayName("존재하지 않는 commentId로 Comment 삭제를 요청할 경우 CommentNotFoundException을 던진다.")
    void throwsCommentNotFoundExceptionWhenCommentIdDoesNotExistOnDeleteComment() {
        // given
        User user = User.create("testUser", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when then
        assertThatThrownBy(() -> commentService.deleteComment(user.getId(), -1))
                .isInstanceOf(CommentNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
    }
}