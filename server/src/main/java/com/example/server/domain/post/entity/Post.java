package com.example.server.domain.post.entity;

import com.example.server.domain.user.entity.User;
import com.example.server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String title;
    private String content;

    public static Post create(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }
}
