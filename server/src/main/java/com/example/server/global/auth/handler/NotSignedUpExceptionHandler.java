package com.example.server.global.auth.handler;

import com.example.server.global.auth.exception.NotSignedUpException;
import com.example.server.global.error.ErrorCode;
import com.example.server.global.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p> OAuth 2.0 인증에 성공하였으나, 반환된 OAuth2UserInfo의 Email로 가입된 User가 없는 경우를 처리합니다.
 * <p> 현재 OAuth2UserInfo를 세션에 저장하고, 세션 ID를 쿠키에 담아 반환합니다.
 */
@RestControllerAdvice
public class NotSignedUpExceptionHandler {

    @ExceptionHandler(NotSignedUpException.class)
    public ResponseEntity<ErrorResponse> handleNotSignedUpException(
            NotSignedUpException e,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 세션 생성 및 OAuth 정보 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("oAuth2User", e.getOAuth2UserInfo());

        // 세션 ID를 쿠키로 설정
        ResponseCookie sessionCookie = ResponseCookie.from("SESSIONID", session.getId())
                .path("/")
                .httpOnly(true)
                .secure(false) // TODO: HTTPS 설정 후 true로 변환
                .sameSite("Lax")
                .maxAge(60 * 10) // 10분
                .build();

        // 쿠키 헤더 추가
        response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());

        return ErrorResponse.toResponseEntity(ErrorCode.REQUIRES_SIGNUP, request.getRequestURI());
    }
}
