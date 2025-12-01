package com.example.server.domain.comment.repository;

import com.example.server.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Slice<Comment> findByPostId(int postId, Pageable pageable);
}
