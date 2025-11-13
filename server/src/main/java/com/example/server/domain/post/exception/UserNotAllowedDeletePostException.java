package com.example.server.domain.post.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UserNotAllowedDeletePostException extends BaseException {
    public UserNotAllowedDeletePostException() {
        super(ErrorCode.USER_NOT_ALLOWED_DELETE_POST);
    }
}
