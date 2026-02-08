package com.ticketplatform.dto.response;

import java.time.LocalDateTime;

public class AuditLogResponse {

    private String username;
    private String action;
    private String resourceType;
    private Long resourceId;
    private LocalDateTime timestamp;
    private String description;

    public AuditLogResponse(String username, String action, String resourceType, Long resourceId, LocalDateTime timestamp, String description) {
        this.username = username;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }
}
