package com.example.server.global.auth.oauth2;

public interface OAuth2Service {
    boolean supports(String provider);
    String getOAuthId(String code);
}
