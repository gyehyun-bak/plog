package com.example.server.global.auth;

import com.example.server.domain.user.domain.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.auth.dto.LoginRequest;
import com.example.server.global.auth.dto.LoginResponse;
import com.example.server.global.auth.dto.ProviderResponse;
import com.example.server.global.auth.exception.OAuth2ProviderNotSupportedException;
import com.example.server.global.auth.exception.UsernameTakenException;
import com.example.server.global.auth.oauth2.OAuth2ServiceManager;
import com.example.server.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2ServiceManager oAuth2ServiceManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public List<ProviderResponse> getProviders() {
        return oAuth2ServiceManager.getProviders();
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        String oAuthId = oAuth2ServiceManager.getOAuthId(request.getProvider(), request.getCode());
        if (oAuthId == null) {
            throw new OAuth2ProviderNotSupportedException();
        }

        User user = userRepository.findByOauthProviderAndOauthId(request.getProvider(), oAuthId)
                .orElse(createNewUser(request.getUsername(), request.getProvider(), oAuthId));

        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        storeRefreshTokenInCookie(response, refreshToken);

        return new LoginResponse(accessToken);
    }

    public void logout(HttpServletResponse response) {
        deleteRefreshTokenFromCookie(response);
    }

    private User createNewUser(String username, String oAuthProvider, String oAuthId) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameTakenException();
        }

        User user = User.create(username, oAuthProvider, oAuthId);
        userRepository.save(user);

        return user;
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
