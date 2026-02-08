package com.ticketplatform.audit;

import com.ticketplatform.service.AuditService;
import com.ticketplatform.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuditInterceptor implements HandlerInterceptor {

    private final AuditService auditService;

    public AuditInterceptor(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        String username = SecurityUtil.currentUsername();

        if (username == null) {
            return;
        }

        String uri = request.getRequestURI();
        
        if (isStaticAsset(uri)) {
            return;
        }
        
        if (uri.contains("/auth/") || uri.endsWith(".html")) {
            return;
        }
    }

    private boolean isStaticAsset(String uri) {
        return uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot).*");
    }
}
