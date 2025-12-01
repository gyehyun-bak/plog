package com.example.server.domain.comment.service;

import com.example.server.domain.comment.dto.response.CommentResponse;
import com.example.server.domain.comment.entity.Comment;
import com.example.server.domain.comment.exception.CommentNotFoundException;
import com.example.server.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResponse getById(int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        return CommentResponse.fromEntity(comment);
    }
}
