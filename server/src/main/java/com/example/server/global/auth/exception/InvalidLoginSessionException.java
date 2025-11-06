package com.example.server.global.auth.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class InvalidLoginSessionException extends BaseException {
    public InvalidLoginSessionException() {
        super(ErrorCode.INVALID_LOGIN_SESSION);
    }
}
