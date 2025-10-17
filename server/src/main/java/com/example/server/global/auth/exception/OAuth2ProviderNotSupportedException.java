package com.example.server.global.auth.exception;

import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;

public class OAuth2ProviderNotSupportedException extends BaseException {
    public OAuth2ProviderNotSupportedException() {
        super(ErrorCode.PROVIDER_NOT_SUPPORTED);
    }
}
