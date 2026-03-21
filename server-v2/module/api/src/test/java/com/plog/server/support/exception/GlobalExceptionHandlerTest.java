package com.plog.server.support.exception;

import com.plog.server.support.log.LogEvent;
import com.plog.server.support.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    GlobalExceptionHandler handler = new GlobalExceptionHandler(publisher);

    @Test
    @DisplayName("Exception 발생 시 반환 body의 타입은 ApiResponse이어야 한다.")
    void shouldReturnApiResponseWhenExceptionIsThrown() {
        // given
        var exception = new Exception();

        // when
        var response = handler.handleException(exception);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ApiResponse.class);
    }

    @Test
    @DisplayName("Exception 발생 시 ApiResponse의 success는 false여야 한다.")
    void shouldSetSuccessFalseWhenExceptionIsThrown() {
        // given
        var exception = new Exception();

        // when
        var response = handler.handleException(exception);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ApiResponse.class);
        assertThat(response.getBody().success()).isFalse();
    }

    @Test
    @DisplayName("Exception 발생 시 ApiResponse의 error는 null이어서는 안 된다.")
    void shouldHaveNonNullErrorWhenExceptionIsThrown() {
        // given
        var exception = new Exception();

        // when
        var response = handler.handleException(exception);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ApiResponse.class);
        assertThat(response.getBody().error()).isNotNull();
    }

    @Test
    @DisplayName("BusinessException 발생 시 응답은 해당 ErrorType과 같은 HTTP 상태 코드를 포함해야 한다.")
    void shouldReturnHttpStatusMatchingErrorTypeWhenBusinessExceptionThrown() {
        // given
        var exception = new BusinessException(ErrorType.DEFAULT_ERROR);

        // when
        var response = handler.handleException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(exception.getErrorType().getStatus());
    }

    @Test
    @DisplayName("BusinessException 발생 시 ErrorMessage는 해당 ErrorType의 ErrorCode를 포함해야 한다.")
    void shouldIncludeErrorCodeWhenBusinessExceptionThrown() {
        // given
        var exception = new BusinessException(ErrorType.DEFAULT_ERROR);

        // when
        var response = handler.handleException(exception);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error().code()).isEqualTo(exception.getErrorType().getCode());
    }

    @Test
    @DisplayName("Exception 발생 시 LogEvent를 발행해야 한다.")
    void shouldPublishLogEventWhenExceptionIsThrown() {
        // given
        var exception = new Exception();

        // when
        handler.handleException(exception);

        // then
        then(publisher).should().publishEvent(any(LogEvent.class));
    }
}