package com.example.server.global.security.userdetails;

import com.example.server.AbstractIntegrationTest;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest extends AbstractIntegrationTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("publicId로 UserDetails를 조회할 수 있다")
    void canLoadUserByUsername() {
        // given
        User user = User.create("test", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getPublicId());

        // then
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertThat(customUserDetails.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("존재하지 않는 publicId로 조회 시 UsernameNotFoundException을 던진다")
    void throwExceptionWhenUserNotFound() {
        // given
        String invalidPublicId = "invalid-public-id";

        // when then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(invalidPublicId))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
