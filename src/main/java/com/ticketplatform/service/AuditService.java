package com.ticketplatform.service;

import com.ticketplatform.dto.response.AuditLogResponse;

import java.util.List;

public interface AuditService {

    void log(String action, String resourceType, Long resourceId, String description);

    List<AuditLogResponse> getTicketLogs(Long ticketId);

    List<AuditLogResponse> getAllLogs();
}
