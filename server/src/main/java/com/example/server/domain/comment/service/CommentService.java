package com.example.server.domain.comment.service;

import com.example.server.domain.comment.dto.response.CommentResponse;
import com.example.server.domain.comment.exception.CommentNotFoundException;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.user.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentService {
    /**
     * 주어진 postId와 사용자 정보를 기반으로 댓글을 작성합니다.
     * @param postId 댓글을 작성할 Post의 고유 식별자
     * @param userId 댓글 작성자 User의 고유 식별자
     * @param content 댓글 내용
     * @return 작성된 댓글 정보를 담은 {@link CommentResponse} 객체
     * @throws PostNotFoundException 지정한 postId의 게시글이 존재하지 않을 경우 발생
     * @throws UserNotFoundException 지정한 userId의 사용자가 존재하지 않을 경우 발생
     */
    CommentResponse createComment(int postId, int userId, String content);

    /**
     * 주어진 commentId를 기준으로 단일 Comment를 조회합니다.
     * @param commentId 조회할 Comment의 고유 식별자
     * @return 조회된 Comment 정보를 담은 {@link CommentResponse} 객체
     * @throws CommentNotFoundException Comment가 존재하지 않을 경우 발생
     */
    CommentResponse getById(int commentId);

    /**
     * 주어진 postId에 연결된 Comment 목록을 페이지 단위로 조회합니다.
     * @param postId 조회할 Post의 고유 식별자
     * @param pageable 페이지 처리 정보를 담은 {@link Pageable} 객체
     * @return 조회된 Comment 정보를 담은 {@link CommentResponse} 객체의 {@link Slice} 컬렉션
     */
    Slice<CommentResponse> getByPostId(int postId, Pageable pageable);
}
