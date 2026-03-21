package com.plog.server.support.log.handler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.plog.server.support.exception.ErrorType;
import com.plog.server.support.log.LogEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

import static org.assertj.core.api.Assertions.assertThat;

class Slf4jLogEventHandlerTest {

    private final Slf4jLogEventHandler slf4jLogEventHandler = new Slf4jLogEventHandler();
    private final Logger logger = (Logger) LoggerFactory.getLogger(Slf4jLogEventHandler.class);
    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

    @AfterEach
    void tearDown() {
        logger.detachAppender(appender);
        appender.stop();
    }

    @ParameterizedTest
    @EnumSource(ErrorType.class)
    @DisplayName("ErrorType의 로그 레벨에 맞는 로깅이 이루어져야 한다.")
    void shouldLogAtExpectedLevelForGivenErrorType(ErrorType errorType) {
        // given
        appender.start();
        logger.addAppender(appender);

        var logEvent = new LogEvent(errorType, new RuntimeException());

        // when
        slf4jLogEventHandler.handleLogEvent(logEvent);

        // then
        if (errorType.getLogLevel() == LogLevel.OFF) {
            assertThat(appender.list).isEmpty();
            return;
        }

        assertThat(appender.list).hasSize(1);

        ILoggingEvent loggingEvent = appender.list.getFirst();

        assertThat(loggingEvent.getLevel()).isEqualTo(toLogbackLevel(errorType.getLogLevel()));
    }

    private Level toLogbackLevel(LogLevel logLevel) {
        return switch (logLevel) {
            case TRACE -> Level.TRACE;
            case DEBUG -> Level.DEBUG;
            case INFO -> Level.INFO;
            case WARN -> Level.WARN;
            case ERROR, FATAL -> Level.ERROR;
            case OFF -> null;
        };
    }
}
