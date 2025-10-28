package com.example.server.domain.user.service;

import com.example.server.domain.user.domain.User;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.error.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("userId로 User를 조회할 수 있다")
    void canGetUserFromUserId() {
        // given
        User user = User.create("test", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when
        User found = userService.getById(user.getId());

        // then
        assertThat(found).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 id의 User 조회 시 예외를 던진다")
    void throwExceptionWhenUserNotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userService.getById(1))
                .isInstanceOf(BaseException.class);
    }
}