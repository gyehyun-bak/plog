package com.plog.server.support.log;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LogEventListener {
    private final List<LogEventHandler> handlers;

    @EventListener
    public void onLogEvent(LogEvent logEvent) {
        for (LogEventHandler handler : handlers) {
            handler.handleLogEvent(logEvent);
        }
    }
}
