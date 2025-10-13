package com.example.server.global.security.userdetails;

import com.example.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return CustomUserDetails.of(userService.getById(Integer.valueOf(username)));
        } catch (Exception e) {
            log.debug("Username not found", e);
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
