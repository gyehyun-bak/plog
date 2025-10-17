package com.example.server.global.auth;

import com.example.server.global.auth.dto.LoginRequest;
import com.example.server.global.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        return null;
    }
}
