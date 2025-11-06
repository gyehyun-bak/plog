package com.example.server.global.auth;

import com.example.server.global.auth.dto.*;
import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request, HttpSession session) {
        OAuth2UserInfo oAuth2UserInfo = (OAuth2UserInfo) session.getAttribute("oAuth2UserInfo");

        if (oAuth2UserInfo == null) {
            throw new BaseException(ErrorCode.INVALID_LOGIN_SESSION);
        }

        SignupResponse response = authService.signup(request.username(), oAuth2UserInfo);
        session.removeAttribute("oAuth2UserInfo");
        return ResponseEntity.ok(response);
    }
}
