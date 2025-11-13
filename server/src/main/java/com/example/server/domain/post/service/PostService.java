package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.request.PostRequest;
import com.example.server.domain.post.dto.response.PostResponse;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.user.exception.UserNotFoundException;

public interface PostService {
    /**
     * 주어진 postId를 기준으로 단일 Post를 조회합니다.
     * @param postId postId 조회할 Post의 고유 식별자.
     * @return 조회된 Post 정보를 담은 {@link PostResponse} 객체.
     * @throws PostNotFoundException Post가 존재하지 않을 경우 발생.
     */
    PostResponse getById(int postId);

    /**
     * 새로운 Post를 생성합니다.
     *
     * <p> 컨트롤러에서 현재 로그인한 사용자의 ID(authorId)를 받아서 생성합니다.
     *
     * @param authorId Post 작성자의 User ID
     * @param postRequest 생성할 Post 정보를 담은 {@link PostRequest} 객체
     * @return 생성된 Post 정보를 담은 {@link PostResponse} 객체
     * @throws UserNotFoundException authorId에 해당하는 User가 존재하지 않을 경우 발생
     */
    PostResponse createPost(int authorId, PostRequest postRequest);
}
