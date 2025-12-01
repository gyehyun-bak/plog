package com.example.server.domain.comment.service;

import com.example.server.domain.comment.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse getById(int commentId);
}
