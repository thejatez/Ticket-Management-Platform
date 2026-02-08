package com.ticketplatform.service.impl;

import com.ticketplatform.dto.response.AuditLogResponse;
import com.ticketplatform.entity.AuditLog;
import com.ticketplatform.entity.User;
import com.ticketplatform.repository.AuditLogRepository;
import com.ticketplatform.repository.UserRepository;
import com.ticketplatform.service.AuditService;
import com.ticketplatform.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository repo;
    private final UserRepository userRepository;

    public AuditServiceImpl(AuditLogRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    @Override
    public void log(String action, String resourceType, Long resourceId, String description) {
        String username = SecurityUtil.currentUsername();
        Long userId = null;
        
        if (username != null) {
            var user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                userId = user.get().getId();
            }
        }

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setResourceType(resourceType);
        log.setResourceId(resourceId);
        log.setUserId(userId);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());

        repo.save(log);
    }

    @Override
    public List<AuditLogResponse> getTicketLogs(Long ticketId) {
        List<AuditLog> logs = repo.findByResourceTypeAndResourceId("TICKET", ticketId);
        List<AuditLogResponse> result = new ArrayList<>();

        for (AuditLog l : logs) {
            String username = "Unknown";
            if (l.getUserId() != null) {
                var user = userRepository.findById(l.getUserId());
                if (user.isPresent()) {
                    username = user.get().getUsername();
                }
            }
            
            result.add(new AuditLogResponse(
                    username,
                    l.getAction(),
                    l.getResourceType(),
                    l.getResourceId(),
                    l.getTimestamp(),
                    l.getDescription()
            ));
        }

        return result;
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {
        List<AuditLog> logs = repo.findAll();
        List<AuditLogResponse> result = new ArrayList<>();

        for (AuditLog l : logs) {
            String username = "Unknown";
            if (l.getUserId() != null) {
                var user = userRepository.findById(l.getUserId());
                if (user.isPresent()) {
                    username = user.get().getUsername();
                }
            }
            
            result.add(new AuditLogResponse(
                    username,
                    l.getAction(),
                    l.getResourceType(),
                    l.getResourceId(),
                    l.getTimestamp(),
                    l.getDescription()
            ));
        }

        return result;
    }
}
