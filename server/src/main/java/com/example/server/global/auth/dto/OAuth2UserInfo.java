package com.example.server.global.auth.dto;

public record OAuth2UserInfo(String oAuthProvider, String oAuthId, String email) {
}
