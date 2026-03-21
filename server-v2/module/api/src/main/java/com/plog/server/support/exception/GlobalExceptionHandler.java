package com.plog.server.support.exception;

import com.plog.server.support.log.LogEvent;
import com.plog.server.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(Exception e) {
        var errorType = extractOrCreateErrorType(e);
        publishLogEvent(errorType, e);

        ApiResponse<Void> response = ApiResponse.error(errorType.getCode(), errorType.getMessage());

        return ResponseEntity.status(errorType.getStatus()).body(response);
    }

    private ErrorType extractOrCreateErrorType(Exception e) {
        ErrorType errorType;
        if (e instanceof BusinessException businessException) {
            errorType = businessException.getErrorType();
        } else {
            errorType = ErrorType.DEFAULT_ERROR;
        }
        return errorType;
    }

    private void publishLogEvent(ErrorType errorType, Exception e) {
        LogEvent logEvent = new LogEvent(errorType, e);
        applicationEventPublisher.publishEvent(logEvent);
    }
}
