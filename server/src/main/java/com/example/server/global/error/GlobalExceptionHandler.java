package com.example.server.global.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        log.info("BaseException 발생: 요청 [{}], 코드 [{}], 메시지 [{}]",
                request.getRequestURI(), e.getErrorCode(), e.getMessage());
        return ErrorResponse.toResponseEntity(e.getErrorCode(), request.getRequestURI());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            HttpServletRequest request) {
        return ErrorResponse.toResponseEntity(ErrorCode.RESOURCE_NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpServletRequest request) {
        return ErrorResponse.toResponseEntity(ErrorCode.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e,
            HttpServletRequest request) {

        log.info("HttpMessageNotReadableException 발생: 요청 [{}], 메시지 [{}]",
                request.getRequestURI(), e.getMessage());

        return ErrorResponse.toResponseEntity(ErrorCode.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        // 필드별 오류 메시지 합치기
        String validationErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.info("ValidationException 발생: 요청 [{}], 메시지 [{}]",
                request.getRequestURI(), validationErrors);

        return ErrorResponse.toResponseEntity(ErrorCode.BAD_REQUEST, request.getRequestURI(), validationErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request) {

        String detailMessage = String.format("파라미터 '%s'는 타입 '%s'이어야 합니다. 입력값: '%s'",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown",
                e.getValue());

        log.info("MethodArgumentTypeMismatchException 발생: 요청 [{}], 메시지 [{}]",
                request.getRequestURI(), detailMessage);

        return ErrorResponse.toResponseEntity(ErrorCode.BAD_REQUEST, request.getRequestURI(), detailMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.warn("예기치 못한 예외 발생: 요청 [{}]", request.getRequestURI(), e);
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }
}
