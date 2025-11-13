package com.example.server.domain.user.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
