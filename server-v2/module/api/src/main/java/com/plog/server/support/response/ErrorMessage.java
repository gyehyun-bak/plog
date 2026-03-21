package com.plog.server.support.response;

import com.plog.server.support.exception.ErrorCode;

public record ErrorMessage(ErrorCode code, String message) {}
