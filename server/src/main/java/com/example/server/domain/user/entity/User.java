package com.example.server.domain.user.entity;

import com.example.server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

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
                .publicId("user_" + UUID.randomUUID().toString())
                .build();
    }

    /**
     * 테스트용 User 객체를 생성합니다. 해당 메서드를 실제 비즈니스 코드에서 사용해서는 안 됩니다.
     * @param userId 테스트용 userId
     * @return userId를 id로 갖는 빈 User
     */
    public static User withId(int userId) {
        return User.builder()
                .id(userId)
                .publicId("user_test_" + UUID.randomUUID().toString())
                .build();
    }

    public void changeUsername(String username) {
        this.username = username;
    }
}
