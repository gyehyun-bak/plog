package com.plog.server.support.log;

import com.plog.server.support.exception.ErrorType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class LogEventListenerTest {

    private final LogEventHandler failingHandler = mock(LogEventHandler.class);
    private final LogEventHandler normalHandler = mock(LogEventHandler.class);
    private final LogEventListener logEventListener = new LogEventListener(List.of(failingHandler, normalHandler));

    @Test
    @DisplayName("LogEvent 발생 시 모든 logEventHandler에 이를 위임해야 한다.")
    void shouldDelegateLogEventToAllHandlers() {
        // given
        var logEvent = new LogEvent(ErrorType.DEFAULT_ERROR, new Exception());

        // when
        logEventListener.onLogEvent(logEvent);

        // then
        then(normalHandler).should(times(1)).handleLogEvent(logEvent);
        then(failingHandler).should(times(1)).handleLogEvent(logEvent);
        then(failingHandler).shouldHaveNoMoreInteractions();
        then(normalHandler).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("특정 handler가 예외를 던져도 다른 handler는 계속 실행되어야 한다.")
    void shouldContinueHandlingWhenHandlerThrowsException() {
        // given
        var logEvent = new LogEvent(ErrorType.DEFAULT_ERROR, new Exception());
        willThrow(new RuntimeException()).given(failingHandler).handleLogEvent(logEvent);

        // when & then
        assertThatCode(() -> logEventListener.onLogEvent(logEvent)).doesNotThrowAnyException();

        then(failingHandler).should(times(1)).handleLogEvent(logEvent);
        then(normalHandler).should(times(1)).handleLogEvent(logEvent);
        then(failingHandler).shouldHaveNoMoreInteractions();
        then(normalHandler).shouldHaveNoMoreInteractions();
    }
}
