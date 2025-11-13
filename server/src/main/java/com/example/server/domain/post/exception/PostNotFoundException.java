package com.example.server.domain.post.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class PostNotFoundException extends BaseException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
