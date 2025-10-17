package com.example.server.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderResponse {
    private String name;
    private String url;
}
