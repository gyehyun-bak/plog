package com.example.server.domain.comment.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class CommentNotFoundException extends BaseException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
