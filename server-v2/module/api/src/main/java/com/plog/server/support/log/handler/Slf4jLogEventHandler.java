package com.plog.server.support.log.handler;

import com.plog.server.support.log.LogEvent;
import com.plog.server.support.log.LogEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Slf4jLogEventHandler implements LogEventHandler {

    @Override
    public void handleLogEvent(LogEvent logEvent) {
        String message = "Exception: ";
        switch (logEvent.errorType().getLogLevel()) {
            case TRACE -> log.trace(message, logEvent.exception());
            case DEBUG -> log.debug(message, logEvent.exception());
            case INFO -> log.info(message, logEvent.exception());
            case WARN -> log.warn(message, logEvent.exception());
            case ERROR -> log.error(message, logEvent.exception());
            default -> throw new IllegalStateException("Unexpected value: " + logEvent.errorType().getLogLevel());
        }
    }
}
