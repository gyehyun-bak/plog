package com.example.server.global.security.jwt;

import com.example.server.global.error.ErrorCode;
import com.example.server.global.security.exception.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        try {
            jwtUtil.validateToken(token); // 여기서 예외가 발생하면 catch로 넘어감

            String userId = jwtUtil.getUserIdFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId); // throws UsernameNotFoundException

            return JwtAuthenticationToken.authenticated(
                    userDetails,
                    token,
                    userDetails.getAuthorities());
        } catch (Exception e) { // JwtException을 AuthenticationException 으로 변환
            throw new JwtAuthenticationException(ErrorCode.JWT_AUTHENTICATION_FAIL);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
