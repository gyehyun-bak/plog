package com.example.server.domain.comment.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UserNotAllowedUpdateCommentException extends BaseException {
    public UserNotAllowedUpdateCommentException() {
        super(ErrorCode.USER_NOT_ALLOWED_UPDATE_COMMENT);
    }
}
