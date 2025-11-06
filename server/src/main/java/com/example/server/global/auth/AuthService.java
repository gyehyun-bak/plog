package com.example.server.global.auth;

import com.example.server.domain.user.domain.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.auth.dto.*;
import com.example.server.global.auth.exception.NotSignedUpException;
import com.example.server.global.auth.exception.OAuth2ProviderNotSupportedException;
import com.example.server.global.auth.exception.UsernameTakenException;
import com.example.server.global.auth.exception.WrongOAuth2ProviderException;
import com.example.server.global.auth.oauth2.OAuth2ServiceManager;
import com.example.server.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2ServiceManager oAuth2ServiceManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public List<ProviderResponse> getProviders() {
        return oAuth2ServiceManager.getProviders();
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        OAuth2UserInfo oAuth2UserInfo = oAuth2ServiceManager.getOAuth2UserInfo(request.provider(), request.code());
        if (oAuth2UserInfo == null) {
            throw new OAuth2ProviderNotSupportedException();
        }

        // Email을 통해 유저 조회, 실패 시 회원가입 절차를 위해 예외 발생
        User user = userRepository.findByEmail(oAuth2UserInfo.email()).orElseThrow(() -> new NotSignedUpException(oAuth2UserInfo));

        // Email은 존재하지만 요청한 Provider로 가입되지 않은 경우
        if (!user.getOauthProvider().equals(request.provider())) {
            throw new WrongOAuth2ProviderException(user.getOauthProvider());
        }

        String accessToken = getAccessToken(response, user);

        return new LoginResponse(accessToken);
    }

    public SignupResponse signup(HttpServletResponse response, String username, OAuth2UserInfo oAuth2UserInfo) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameTakenException();
        }

        User user = User.create(username, oAuth2UserInfo.email(), oAuth2UserInfo.oAuthProvider(), oAuth2UserInfo.oAuthId());
        userRepository.save(user);

        return new SignupResponse(getAccessToken(response, user));
    }

    private String getAccessToken(HttpServletResponse response, User user) {
        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        storeRefreshTokenInCookie(response, refreshToken);
        return accessToken;
    }

    public void logout(HttpServletResponse response) {
        deleteRefreshTokenFromCookie(response);
    }

    private void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(jwtUtil.getRefreshTokenMaxAgeInSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private void deleteRefreshTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
