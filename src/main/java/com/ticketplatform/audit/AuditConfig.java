package com.ticketplatform.audit;

import com.ticketplatform.service.AuditService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class AuditConfig implements WebMvcConfigurer {

    private final AuditService auditService;

    public AuditConfig(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuditInterceptor(auditService));
    }
}
