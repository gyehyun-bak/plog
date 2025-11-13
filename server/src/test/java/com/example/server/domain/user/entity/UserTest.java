package com.example.server.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("User 생성")
    void createUser() {
        // given
        String username = "username";
        String email = "test@email.com";
        String oAuthProvider = "PROVIDER";
        String oAuthId = "oAuthId";

        // when
        User user = User.create(username, email, oAuthProvider, oAuthId);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getOauthProvider()).isEqualTo(oAuthProvider);
        assertThat(user.getOauthId()).isEqualTo(oAuthId);
    }

    @Test
    @DisplayName("username 변경")
    void changeUsername() {
        // given
        String oldUsername = "old";
        User user = User.create(oldUsername, "", "", "");
        String newUsername = "new";

        // when
        user.changeUsername(newUsername);

        // then
        assertThat(user.getUsername()).isEqualTo(newUsername);
    }
}