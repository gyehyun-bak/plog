package com.example.server.global.auth.handler;

import com.example.server.global.auth.dto.NotSignedUpResponse;
import com.example.server.global.auth.exception.NotSignedUpException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotSignedUpExceptionHandler {

    @ExceptionHandler(NotSignedUpException.class)
    public ResponseEntity<NotSignedUpResponse> handleNotSignedUpException(
            NotSignedUpException e,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok().build();
    }
}
