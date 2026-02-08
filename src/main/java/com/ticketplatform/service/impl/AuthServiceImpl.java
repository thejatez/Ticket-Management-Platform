package com.ticketplatform.service.impl;

import com.ticketplatform.dto.request.LoginRequest;
import com.ticketplatform.dto.request.RegisterRequest;
import com.ticketplatform.dto.response.LoginResponse;
import com.ticketplatform.entity.Role;
import com.ticketplatform.entity.User;
import com.ticketplatform.repository.UserRepository;
import com.ticketplatform.security.JwtUtil;
import com.ticketplatform.service.AuthService;
import com.ticketplatform.service.AuditService;
import com.ticketplatform.service.CacheService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;
    private final CacheService cacheService;

    public AuthServiceImpl(UserRepository repository,
                           PasswordEncoder encoder,
                           JwtUtil jwtUtil,
                           AuditService auditService,
                           CacheService cacheService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
        this.cacheService = cacheService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        
        try {
            auditService.log("LOGIN", "USER", user.getId(), "User logged in: " + user.getUsername());
        } catch (Exception e) {
        }

        return new LoginResponse(token, user.getRole().name());
    }

    @Override
    public void register(RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        repository.save(user);
        
        // Invalidate users cache after creating a new user
        cacheService.evict("users:all");
        
        try {
            auditService.log("REGISTER", "USER", user.getId(), "User registered: " + request.getUsername());
        } catch (Exception e) {
        }
    }
}

