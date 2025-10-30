package com.example.server.global.auth.exception;

import lombok.Getter;

@Getter
public class WrongOAuth2ProviderException extends RuntimeException {
    private final String provider;
    public WrongOAuth2ProviderException(String oauthProvider) {
        this.provider = oauthProvider;
    }
}
