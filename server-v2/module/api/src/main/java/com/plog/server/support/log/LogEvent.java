package com.plog.server.support.log;

import com.plog.server.support.exception.ErrorType;

public record LogEvent(ErrorType errorType, Exception exception) {}
