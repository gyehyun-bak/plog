package com.example.server.global.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String provider;
    private String code;
}
