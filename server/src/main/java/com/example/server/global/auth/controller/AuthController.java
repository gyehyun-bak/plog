package com.example.server.global.auth.controller;

import com.example.server.global.auth.service.AuthService;
import com.example.server.global.auth.dto.SignupRequest;
import com.example.server.global.auth.dto.*;
import com.example.server.global.auth.exception.InvalidLoginSessionException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/providers")
    public ResponseEntity<List<ProviderResponse>> getProviders() {
        return ResponseEntity.ok(authService.getProviders());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            HttpServletResponse servletResponse,
            @RequestBody @Validated SignupRequest request,
            HttpSession session
    ) {
        OAuth2UserInfo oAuth2UserInfo = (OAuth2UserInfo) session.getAttribute("oAuth2UserInfo");

        // 기존에 만들어진 로그인 세션이 없는 경우 예외 발생
        if (oAuth2UserInfo == null) {
            throw new InvalidLoginSessionException();
        }

        SignupResponse response = authService.signup(servletResponse, request.username(), oAuth2UserInfo);
        session.removeAttribute("oAuth2UserInfo");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-username")
    public ResponseEntity<ValidateUsernameResponse> checkUsername(
            @RequestBody @Validated ValidateUsernameRequest request
    ) {
        return ResponseEntity.ok(authService.validateUsername(request.username()));
    }
}
