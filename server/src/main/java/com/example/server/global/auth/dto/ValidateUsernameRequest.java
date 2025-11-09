package com.example.server.global.auth.dto;

import com.example.server.global.auth.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;

public record ValidateUsernameRequest(
        @NotBlank(message = "username은 필수입니다.") @ValidUsername String username) {
}
