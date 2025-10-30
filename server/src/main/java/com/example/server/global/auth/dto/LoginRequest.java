package com.example.server.global.auth.dto;

public record LoginRequest(String provider, String code) {
}
