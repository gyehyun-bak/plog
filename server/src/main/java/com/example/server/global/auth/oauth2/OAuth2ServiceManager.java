package com.example.server.global.auth.oauth2;

import com.example.server.global.auth.dto.OAuth2UserInfo;
import com.example.server.global.auth.dto.ProviderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2ServiceManager {
    private final List<OAuth2Service> services;

    public List<ProviderResponse> getProviders() {
        return services.stream().map(OAuth2Service::getProvider).toList();
    }

    public OAuth2UserInfo getOAuth2UserInfo(String provider, String code) {
        for (OAuth2Service service : services) {
            if (service.supports(provider)) {
                return service.getOAuth2Response(code);
            }
        }
        return null;
    }
}
