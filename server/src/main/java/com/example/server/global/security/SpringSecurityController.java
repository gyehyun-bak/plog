package com.example.server.global.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringSecurityController {

    @GetMapping("/test/anonymous")
    public String anonymous() {
        return "Hello anonymous";
    }

    @GetMapping("/test/authenticated")
    public String authenticated() {
        return "Hello authenticated";
    }
}
