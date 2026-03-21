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
            case TRACE -> log.trace(message, logEvent.e());
            case DEBUG -> log.debug(message, logEvent.e());
            case INFO -> log.info(message, logEvent.e());
            case WARN -> log.warn(message, logEvent.e());
            case ERROR -> log.error(message, logEvent.e());
            default -> throw new IllegalStateException("Unexpected value: " + logEvent.errorType().getLogLevel());
        }
    }
}
