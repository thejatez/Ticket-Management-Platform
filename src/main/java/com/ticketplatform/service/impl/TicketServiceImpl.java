package com.ticketplatform.service.impl;

import com.ticketplatform.dto.request.TicketCreateRequest;
import com.ticketplatform.dto.request.TicketUpdateRequest;
import com.ticketplatform.dto.response.TicketResponse;
import com.ticketplatform.entity.*;
import com.ticketplatform.exception.UnauthorizedException;
import com.ticketplatform.repository.TicketRepository;
import com.ticketplatform.repository.UserRepository;
import com.ticketplatform.service.AuditService;
import com.ticketplatform.service.CacheService;
import com.ticketplatform.service.TicketService;
import com.ticketplatform.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final AuditService auditService;
    private final CacheService cacheService;

    public TicketServiceImpl(TicketRepository ticketRepo,
                             UserRepository userRepo,
                             AuditService auditService,
                             CacheService cacheService) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.auditService = auditService;
        this.cacheService = cacheService;
    }

    private String getPerformerInfo(User user) {
        return user.getRole().equals(Role.ADMIN) ? "[ADMIN: " + user.getUsername() + "]" : "";
    }

    @Override
    public TicketResponse createTicket(TicketCreateRequest request) {

        String username = SecurityUtil.currentUsername();
        User user = userRepo.findByUsername(username).orElseThrow();

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(user);
        ticket.setCreatedAt(LocalDateTime.now());

        ticketRepo.save(ticket);

        cacheService.evict("tickets:user:" + username);
        cacheService.evict("tickets:all");

        auditService.log("CREATE", "TICKET", ticket.getId(), "Ticket created: " + ticket.getTitle() + " " + getPerformerInfo(user));

        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().toString());
    }

    @Override
    public List<TicketResponse> getMyTickets() {

        String username = SecurityUtil.currentUsername();
        String cacheKey = "tickets:user:" + username;

        @SuppressWarnings("unchecked")
        List<TicketResponse> cachedTickets = (List<TicketResponse>) cacheService.get(cacheKey);
        if (cachedTickets != null) {
            return cachedTickets;
        }

        User user = userRepo.findByUsername(username).orElseThrow();

        List<Ticket> tickets = ticketRepo.findByCreatedBy(user);

        List<TicketResponse> result = new ArrayList<>();

        for (Ticket t : tickets) {
            result.add(new TicketResponse(
                    t.getId(),
                    t.getTitle(),
                    t.getDescription(),
                    t.getStatus().name()
            ));
        }

        cacheService.put(cacheKey, result);

        return result;
    }

    @Override
    public void updateTicket(Long id, TicketUpdateRequest request) {

        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        
        String username = SecurityUtil.currentUsername();
        User user = userRepo.findByUsername(username).orElseThrow();
        
        if (!ticket.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only ticket owner or admin can update ticket");
        }

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.valueOf(request.getStatus()));

        ticketRepo.save(ticket);

        cacheService.evict("tickets:user:" + username);
        cacheService.evict("tickets:user:" + ticket.getCreatedBy().getUsername());
        cacheService.evict("tickets:all");

        auditService.log("UPDATE", "TICKET", id, "Ticket updated: " + ticket.getTitle() + " " + getPerformerInfo(user));
    }

    @Override
    public void closeTicket(Long id) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        
        String username = SecurityUtil.currentUsername();
        User user = userRepo.findByUsername(username).orElseThrow();
        
        if (!ticket.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only ticket owner or admin can close ticket");
        }
        
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());
        ticketRepo.save(ticket);
        
        cacheService.evict("tickets:user:" + username);
        cacheService.evict("tickets:user:" + ticket.getCreatedBy().getUsername());
        cacheService.evict("tickets:all");
        
        auditService.log("CLOSE", "TICKET", id, "Ticket closed: " + ticket.getTitle() + " " + getPerformerInfo(user));
    }

    @Override
    public void reopenTicket(Long id) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        
        String username = SecurityUtil.currentUsername();
        User user = userRepo.findByUsername(username).orElseThrow();
        
        if (!ticket.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only ticket owner or admin can reopen ticket");
        }
        
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setReopenedAt(LocalDateTime.now());
        ticketRepo.save(ticket);
        
        cacheService.evict("tickets:user:" + username);
        cacheService.evict("tickets:user:" + ticket.getCreatedBy().getUsername());
        cacheService.evict("tickets:all");
        
        auditService.log("REOPEN", "TICKET", id, "Ticket reopened: " + ticket.getTitle() + " " + getPerformerInfo(user));
    }

    @Override
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        
        String username = SecurityUtil.currentUsername();
        User user = userRepo.findByUsername(username).orElseThrow();
        
        if (!ticket.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only ticket owner or admin can delete ticket");
        }
        
        ticketRepo.deleteById(id);
        
        cacheService.evict("tickets:user:" + username);
        cacheService.evict("tickets:user:" + ticket.getCreatedBy().getUsername());
        cacheService.evict("tickets:all");
        
        auditService.log("DELETE", "TICKET", id, "Ticket deleted: " + ticket.getTitle() + " " + getPerformerInfo(user));
    }
}
