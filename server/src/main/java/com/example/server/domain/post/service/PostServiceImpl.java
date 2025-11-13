package com.example.server.domain.post.service;

import com.example.server.domain.post.dto.request.PostRequest;
import com.example.server.domain.post.dto.response.PostResponse;
import com.example.server.domain.post.entity.Post;
import com.example.server.domain.post.exception.PostNotFoundException;
import com.example.server.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponse getById(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return PostResponse.fromEntity(post);
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        return null;
    }
}
