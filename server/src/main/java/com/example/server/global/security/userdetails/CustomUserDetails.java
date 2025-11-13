package com.example.server.global.security.userdetails;

import com.example.server.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public static CustomUserDetails of(User member) {
        return new CustomUserDetails(member);
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
//                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
