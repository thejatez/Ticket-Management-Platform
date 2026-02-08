package com.ticketplatform.interceptor;

import com.ticketplatform.exception.RateLimitExceededException;
import com.ticketplatform.service.RateLimitService;
import com.ticketplatform.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String username = SecurityUtil.currentUsername();

        if (username == null) {
            return true;
        }

        String uri = request.getRequestURI();

        if (isStaticAsset(uri) || uri.contains("/auth/")) {
            return true;
        }

        if (!rateLimitService.allowRequest(username)) {
            throw new RateLimitExceededException("Rate limit exceeded. Maximum 100 requests allowed.");
        }

        return true;
    }

    private boolean isStaticAsset(String uri) {
        return uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot).*");
    }
}
