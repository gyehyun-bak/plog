package com.example.server.domain.post.entity;

import com.example.server.domain.user.entity.User;
import com.example.server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String title;
    private String content;

    private LocalDateTime updatedAt;

    public static Post create(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 테스트용 Post 객체를 생성합니다. 해당 메서드를 실제 비즈니스 코드에서 사용해서는 안 됩니다.
     * @param postId 테스트용 postId
     * @return postId를 id로 갖는 빈 Post
     */
    public static Post withId(Integer postId) {
        return Post.builder()
                .id(postId)
                .build();
    }

    public void updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
