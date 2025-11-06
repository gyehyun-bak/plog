package com.example.server.global.auth;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "username은 필수입니다.") String username) {
}
