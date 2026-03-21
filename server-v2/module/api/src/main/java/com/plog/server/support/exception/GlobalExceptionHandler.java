package com.plog.server.support.exception;

import com.plog.server.support.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        var errorType = extractErrorType(e);
        ApiResponse<Void> response = ApiResponse.error(errorType.getCode(), errorType.getMessage());

        return ResponseEntity.status(errorType.getStatus()).body(response);
    }

    private ErrorType extractErrorType(Exception e) {
        ErrorType errorType;
        if (e instanceof BusinessException) {
            errorType = ((BusinessException) e).getErrorType();
        } else {
            errorType = ErrorType.DEFAULT_ERROR;
        }
        // TODO: 공통 Error Event 발행
        return errorType;
    }
}
