package com.plog.server.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DEFAULT_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생하였습니다.", LogLevel.ERROR);

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;
}
