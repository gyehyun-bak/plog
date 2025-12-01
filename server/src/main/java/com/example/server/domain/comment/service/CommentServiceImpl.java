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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponse createComment(int postId, int userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Comment comment = Comment.create(user, post, content);
        commentRepository.save(comment);

        return CommentResponse.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentResponse getById(int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        return CommentResponse.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<CommentResponse> getByPostId(int postId, Pageable pageable) {
        Slice<Comment> commentSlice = commentRepository.findByPostId(postId, pageable);
        return commentSlice.map(CommentResponse::fromEntity);
    }
}
