package com.example.server.global.auth.exception;

import com.example.server.global.auth.dto.OAuth2UserInfo;
import lombok.Getter;

@Getter
public class NotSignedUpException extends RuntimeException {
    private final OAuth2UserInfo oAuth2UserInfo;

    public NotSignedUpException(OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
    }
}
