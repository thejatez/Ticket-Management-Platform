package com.ticketplatform.controller;

import com.ticketplatform.dto.response.AuditLogResponse;
import com.ticketplatform.entity.Role;
import com.ticketplatform.entity.User;
import com.ticketplatform.exception.UnauthorizedException;
import com.ticketplatform.repository.TicketRepository;
import com.ticketplatform.repository.UserRepository;
import com.ticketplatform.service.AuditService;
import com.ticketplatform.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public AuditController(AuditService auditService, TicketRepository ticketRepository, UserRepository userRepository) {
        this.auditService = auditService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/ticket/{id}")
    public List<AuditLogResponse> getTicketLogs(@PathVariable Long id) {
        var ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        String username = SecurityUtil.currentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!ticket.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only ticket owner or admin can view ticket audit history");
        }
        
        return auditService.getTicketLogs(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLogResponse> getAllLogs() {
        return auditService.getAllLogs();
    }
}
