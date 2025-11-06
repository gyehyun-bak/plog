package com.example.server.global.auth.oauth2;

import com.example.server.global.auth.dto.OAuth2UserInfo;
import com.example.server.global.auth.dto.ProviderResponse;

public interface OAuth2Service {
    boolean supports(String provider);
    OAuth2UserInfo getOAuth2UserInfo(String code);
    ProviderResponse getProvider();
}
