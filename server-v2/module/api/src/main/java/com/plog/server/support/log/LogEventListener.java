package com.plog.server.support.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogEventListener {
    private final List<LogEventHandler> handlers;

    @EventListener
    public void onLogEvent(LogEvent logEvent) {
        for (LogEventHandler handler : handlers) {
            try {
                handler.handleLogEvent(logEvent);
            } catch (Exception e) {
                log.error("Error while handling log event", e);
            }
        }
    }
}
