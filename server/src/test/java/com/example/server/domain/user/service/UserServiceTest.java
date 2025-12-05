package com.example.server.domain.user.service;

import com.example.server.AbstractIntegrationTest;
import com.example.server.domain.user.dto.response.UserResponse;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.exception.UserNotFoundException;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.global.error.BaseException;
import com.example.server.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("publicId로 User를 조회할 수 있다")
    void canGetUserFromPublicId() {
        // given
        User user = User.create("test", "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when
        UserResponse found = userService.getById(user.getPublicId());

        // then
        assertThat(found.id()).isEqualTo(user.getPublicId());
        assertThat(found.username()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("존재하지 않는 id의 User 조회 시 USER_NOT_FOUND 예외를 던진다")
    void throwExceptionWhenUserNotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userService.getById("1234"))
                .isInstanceOf(UserNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("사용 가능한 username은 예외를 던지지 않는다")
    void doesntThrowExceptionWhenUsernameValid() {
        // given
        String username = "test";

        // when then
        userService.validateUsername(username);
    }

    @Test
    @DisplayName("이미 사용 중인 username은 USERNAME_TAKEN 예외를 던진다")
    void throwExceptionWhenUsernameTaken() {
        // given
        String username = "test";
        User user = User.create(username, "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        // when
        // then
        assertThatThrownBy(() -> userService.validateUsername(username))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USERNAME_TAKEN);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", ".", "user..name", ".username", "username.", "user@name", "thisusernameiswaytoolongtobevalid123" })
    @DisplayName("형식이 잘못된 username은 INVALID_USERNAME 예외를 던진다")
    void throwExceptionWhenUsernameInvalidFormat(String invalidUsername) {
        assertThatThrownBy(() -> userService.validateUsername(invalidUsername))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_USERNAME);
    }

    @Test
    @DisplayName("올바른 username 으로 바꿀 수 있다")
    void canChangeToValidUsername() {
        // given
        String username = "test";
        User user = User.create(username, "test@email.com", "TEST", "test-id");
        userRepository.save(user);

        String newUsername = "new";

        // when
        userService.changeUsername(user.getId(), newUsername);

        // then
        assertThat(user.getUsername()).isEqualTo(newUsername);
    }
}