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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public PostResponse getById(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return PostResponse.fromEntity(post);
    }

    @Override
    public PostResponse createPost(int authorId, PostRequest postRequest) {
        User author = userRepository.findById(authorId).orElseThrow(UserNotFoundException::new);
        Post post = Post.create(author, postRequest.title(), postRequest.content());
        postRepository.save(post);

        return PostResponse.fromEntity(post);
    }

    @Override
    public PostResponse updatePost(int userId, int postId, PostRequest postRequest) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if (!post.getAuthor().equals(user)) {
            throw new UserNotAllowedUpdatePostException();
        }

        if (postRequest.title() != null && !postRequest.title().isEmpty() && !post.getTitle().equals(postRequest.title())) {
            post.updateTitle(postRequest.title());
        }

        if (postRequest.content() != null && !postRequest.content().isEmpty() &&!post.getContent().equals(postRequest.content())) {
            post.updateContent(postRequest.content());
        }

        return PostResponse.fromEntity(post);
    }
}
