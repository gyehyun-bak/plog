package com.example.server.global.auth;

import com.example.server.global.auth.dto.LoginRequest;
import com.example.server.global.auth.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("올바른 요청에 Access/Refresh Token을 반환한다")
    void returnAccessAndRefreshTokenWhenValidRequest() {
        // given
        LoginRequest request = new LoginRequest();
        var servletResponse = new MockHttpServletResponse();

        // when
        LoginResponse response = authService.login(request, servletResponse);

        // then
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(servletResponse.getCookies()).isNotNull();
        assertThat(servletResponse.getCookies()).isNotEmpty();
        assertThat(servletResponse.getCookies()[0].getName()).isEqualTo("refreshToken");
    }
}