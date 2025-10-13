package com.example.server.global.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest
@Import(SecurityConfig.class)
class SpringSecurityControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Test
    @DisplayName("익명 사용자는 /test/anonymous에 접근 가능")
    @WithAnonymousUser
    void anonymousUserCanAccessAnonymousEndpoint() {
        assertThat(mockMvc.get().uri("/test/anonymous"))
                .hasStatusOk();
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 /test/anonymous에 접근 가능")
    void authenticatedMemberCanAccessAnonymousEndpoint() {
        assertThat(mockMvc.get().uri("/test/anonymous"))
                .hasStatusOk();
    }

    @Test
    @DisplayName("익명 사용자는 /test/anonymous에 접근 불가, 401 반환")
    @WithAnonymousUser
    void anonymousUserCannotAccessAuthenticatedEndpoint() {
        assertThat(mockMvc.get().uri("/test/authenticated"))
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 /test/authenticated에 접근 가능")
    void authenticatedMemberCanAccessAuthenticatedEndpoint() {
        assertThat(mockMvc.get().uri("/test/authenticated"))
                .hasStatusOk();
    }
}