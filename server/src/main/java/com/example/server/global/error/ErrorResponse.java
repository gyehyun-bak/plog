package com.example.server.global.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String path;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 기본 메시지 사용
     */
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode code, String path) {
        return toResponseEntity(code, path, null);
    }

    /**
     * 커스텀 메시지 사용
     */
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode code, String path, String message) {
        ErrorResponse body = ErrorResponse.builder()
                .status(code.getStatus().value())
                .error(message != null ? message : code.getMessage())
                .path(path)
                .build();
        return ResponseEntity.status(code.getStatus()).body(body);
    }

    public static ErrorResponse of(ErrorCode code, String path) {
        return ErrorResponse.builder()
                .status(code.getStatus().value())
                .error(code.getMessage())
                .path(path)
                .build();
    }
}
