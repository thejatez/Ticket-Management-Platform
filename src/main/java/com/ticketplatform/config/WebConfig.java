package com.ticketplatform.config;

import com.ticketplatform.interceptor.RateLimitInterceptor;
import com.ticketplatform.service.RateLimitService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitService rateLimitService;

    public WebConfig(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitService))
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/index.html", "/login.html", "/register.html");
    }
}
