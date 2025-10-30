package com.example.server.global.auth.oauth2;

import com.example.server.global.auth.dto.OAuth2UserInfo;
import com.example.server.global.auth.dto.ProviderResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class GoogleOAuth2Service implements OAuth2Service {

    private static final String PROVIDER_NAME = "GOOGLE";

    private final RestTemplate restTemplate = new RestTemplate();

    private final String tokenUri = "https://oauth2.googleapis.com/token";
    private final String userInfoUri = "https://www.googleapis.com/oauth2/v3/userinfo";
    private final String authUri = "https://accounts.google.com/o/oauth2/v2/auth";

    @Value("${oauth.google.client-id}")
    private String clientId;
    @Value("${oauth.google.client-secret}")
    private String clientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;


    @Override
    public ProviderResponse getProvider() {
        return new ProviderResponse(PROVIDER_NAME, getAuthorizationUrl());
    }

    private String getAuthorizationUrl() {
        return authUri + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=email%20profile";
    }

    @Override
    public boolean supports(String provider) {
        return provider.equals(PROVIDER_NAME);
    }

    @Override
    public OAuth2UserInfo getOAuth2Response(String code) {
        GoogleTokenResponse tokenResponse = getAccessToken(code);
        GoogleUserInfo userInfo = getUserInfo(tokenResponse.getAccessToken());
        return new OAuth2UserInfo(userInfo.sub, userInfo.getEmail());
    }

    private GoogleTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + code +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // TODO: Authorization Code로 인증 실패 시 응답 처리
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                tokenUri,
                request,
                GoogleTokenResponse.class
        );

        return response.getBody();
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                GoogleUserInfo.class
        );

        return response.getBody();
    }

    @Data
    public static class GoogleTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Data
    private static class GoogleUserInfo {
        private String sub;
        private String email;
        private String name;
    }
}
