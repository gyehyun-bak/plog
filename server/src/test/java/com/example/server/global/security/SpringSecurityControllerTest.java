package com.example.server.global.security;

import com.example.server.domain.user.domain.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = User.create("testUser", "test@email.com", "TEST", "test-social-id");
        userRepository.save(testUser);
    }

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

    @Test
    @DisplayName("유효한 JWT 엑세스 토큰으로 /test/authenticated에 접근 가능")
    void jwtCanBeValidated() {
        String accessToken = jwtUtil.createAccessToken(testUser.getId());
        assertThat(mockMvc.get()
                .header("Authorization", "Bearer " + accessToken)
                .uri("/test/authenticated"))
                .hasStatusOk();
    }

    @Test
    @DisplayName("유효하지 않은 JWT 엑세스 토큰인 경우 /test/authenticated에 접근 불가")
    void withoutJwtHeader() {
        assertThat(mockMvc.get()
                .header("Authorization", "Bearer " + "Hello World.")
                .uri("/test/authenticated"))
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }
}