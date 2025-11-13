package com.example.server.domain.post.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UserNotAllowedUpdatePostException extends BaseException {
    public UserNotAllowedUpdatePostException() {
        super(ErrorCode.USER_NOT_ALLOWED_UPDATE_POST);
    }
}
