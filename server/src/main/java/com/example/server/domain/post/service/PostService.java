package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.response.PostResponse;

public interface PostService {
    /**
     * 주어진 postId를 기준으로 단일 Post를 조회합니다.
     * @param postId postId 조회할 Post의 고유 식별자.
     * @return 조회된 Post 정보를 담은 {@link PostResponse} 객체.
     */
    PostResponse getById(int postId);
}
