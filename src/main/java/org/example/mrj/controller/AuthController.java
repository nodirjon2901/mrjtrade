package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.Token;
import org.example.mrj.security.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor

@Controller
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Token>> login(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        return userService.login(username, password);
    }
}
