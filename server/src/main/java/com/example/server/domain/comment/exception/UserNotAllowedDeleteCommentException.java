package com.example.server.domain.comment.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UserNotAllowedDeleteCommentException extends BaseException {
    public UserNotAllowedDeleteCommentException() {
        super(ErrorCode.USER_NOT_ALLOWED_DELETE_COMMENT);
    }
}
