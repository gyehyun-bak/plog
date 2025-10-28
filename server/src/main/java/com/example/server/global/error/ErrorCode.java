package com.example.server.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // BAD REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 필요한 요청 데이터가 없거나, 지원되지 않는 형식입니다."),
    PROVIDER_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth Provider 입니다."),
    USERNAME_TAKEN(HttpStatus.BAD_REQUEST, "이미 사용 중인 username 입니다."),

    // NOT FOUND
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다. URL을 확인하세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다."),

    // METHOD NOT ALLOWED
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),

    // UNAUTHORIZED
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요한 요청입니다."),
    JWT_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "JWT 인증에 실패하였습니다."),

    // ETC
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String message;
}
