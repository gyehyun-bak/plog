package com.example.server.global.auth.oauth2;

import com.example.server.global.auth.dto.OAuth2Response;
import com.example.server.global.auth.dto.ProviderResponse;

public interface OAuth2Service {
    boolean supports(String provider);
    OAuth2Response getOAuthId(String code);
    ProviderResponse getProvider();
}
