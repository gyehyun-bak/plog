package com.example.server.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(nullable = false, unique = true)
    private String username;
    private String oauthProvider;
    private String oauthId;

    public static User create(String username, String oauthProvider, String oauthId) {
        return User.builder()
                .username(username)
                .oauthProvider(oauthProvider)
                .oauthId(oauthId)
                .build();
    }
}
