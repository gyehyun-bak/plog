package com.example.server.global.auth.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class UsernameTakenException extends BaseException {
    public UsernameTakenException() {
        super(ErrorCode.USERNAME_TAKEN);
    }
}
