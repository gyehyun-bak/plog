package com.example.server.domain.user.domain;

import com.example.server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String oauthProvider;
    private String oauthId;

    public static User create(String username, String email, String oauthProvider, String oauthId) {
        return User.builder()
                .username(username)
                .email(email)
                .oauthProvider(oauthProvider)
                .oauthId(oauthId)
                .build();
    }

    public void changeUsername(String username) {
        this.username = username;
    }
}
