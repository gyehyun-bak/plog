package com.example.server.domain.post.entity;

import com.example.server.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    @DisplayName("Post 생성")
    void create() {
        // given
        int userId = 0;
        String title = "title";
        String content = "content";
        User author = User.withId(userId); // 테스트용 객체 생성

        // when
        Post post = Post.create(author, title, content);

        // then
        assertThat(post).isNotNull();
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("Post 수정")
    void editPost() {
        // given
        int userId = 0;
        String title = "title";
        String content = "content";
        User author = User.withId(userId); // 테스트용 객체 생성

        Post post = Post.create(author, title, content);

        // when
        String newTitle = "newTitle";
        String newContent = "newContent";
        post.edit(newTitle, newContent);

        // then
        assertThat(post.getTitle()).isEqualTo(newTitle);
        assertThat(post.getContent()).isEqualTo(newContent);
    }
}