package com.plog.server.support.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorType errorType;

    public BusinessException(ErrorType errorType) {
        super(errorType.name());
        this.errorType = errorType;
    }

    public BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public BusinessException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public BusinessException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }
}
