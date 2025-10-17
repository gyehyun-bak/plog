package com.example.server.global.auth.oauth2;

import com.example.server.global.auth.dto.ProviderResponse;

public interface OAuth2Service {
    boolean supports(String provider);
    String getOAuthId(String code);
    ProviderResponse getProvider();
}
