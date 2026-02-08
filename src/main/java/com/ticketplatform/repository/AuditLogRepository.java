package com.ticketplatform.repository;

import com.ticketplatform.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByResourceId(Long resourceId);

    List<AuditLog> findByResourceTypeAndResourceId(String resourceType, Long resourceId);

    List<AuditLog> findByUserId(Long userId);
}
