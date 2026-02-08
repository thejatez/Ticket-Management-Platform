package com.ticketplatform.controller;

import com.ticketplatform.dto.request.LoginRequest;
import com.ticketplatform.dto.request.RegisterRequest;
import com.ticketplatform.dto.response.LoginResponse;
import com.ticketplatform.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request);
    }
}
