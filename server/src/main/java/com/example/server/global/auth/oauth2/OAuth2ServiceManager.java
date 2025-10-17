package com.example.server.global.auth.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2ServiceManager {
    private final List<OAuth2Service> services;


}
