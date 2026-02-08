package com.ticketplatform.service;

import com.ticketplatform.dto.request.LoginRequest;
import com.ticketplatform.dto.request.RegisterRequest;
import com.ticketplatform.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);
}
