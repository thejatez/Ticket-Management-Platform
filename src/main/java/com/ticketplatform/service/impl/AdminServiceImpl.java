package com.ticketplatform.service.impl;

import com.ticketplatform.dto.response.TicketResponse;
import com.ticketplatform.dto.response.UserDetailResponse;
import com.ticketplatform.entity.Role;
import com.ticketplatform.entity.Ticket;
import com.ticketplatform.entity.User;
import com.ticketplatform.repository.TicketRepository;
import com.ticketplatform.repository.UserRepository;
import com.ticketplatform.service.AdminService;
import com.ticketplatform.service.AuditService;
import com.ticketplatform.service.CacheService;
import com.ticketplatform.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final TicketRepository repo;
    private final AuditService auditService;
    private final CacheService cacheService;
    private final UserRepository userRepository;

    public AdminServiceImpl(TicketRepository repo,
                            AuditService auditService,
                            CacheService cacheService,
                            UserRepository userRepository) {
        this.repo = repo;
        this.auditService = auditService;
        this.cacheService = cacheService;
        this.userRepository = userRepository;
    }

    @Override
    public List<TicketResponse> getAllTickets() {

        String cacheKey = "tickets:all";

        @SuppressWarnings("unchecked")
        List<TicketResponse> cachedTickets = (List<TicketResponse>) cacheService.get(cacheKey);
        if (cachedTickets != null) {
            return cachedTickets;
        }

        List<Ticket> tickets = repo.findAll();
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
    public void deleteTicket(Long id) {

        repo.deleteById(id);
        
        cacheService.evict("tickets:all");

        String username = SecurityUtil.currentUsername();
        String adminInfo = " [ADMIN: " + username + "]";

        auditService.log("DELETE", "TICKET", id, "Ticket deleted with ID: " + id + adminInfo);
    }

    @Override
    public List<UserDetailResponse> getAllUsers() {

        String cacheKey = "users:all";

        @SuppressWarnings("unchecked")
        List<UserDetailResponse> cachedUsers = (List<UserDetailResponse>) cacheService.get(cacheKey);
        if (cachedUsers != null) {
            return cachedUsers;
        }

        List<User> users = userRepository.findAll();
        List<UserDetailResponse> result = new ArrayList<>();

        for (User u : users) {
            List<Ticket> userTickets = repo.findByCreatedBy(u);
            result.add(new UserDetailResponse(
                    u.getId(),
                    u.getUsername(),
                    u.getRole().name(),
                    userTickets.size()
            ));
        }

        cacheService.put(cacheKey, result);

        return result;
    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Cannot delete admin users");
        }

        // Delete all tickets associated with the user first
        List<Ticket> userTickets = repo.findByCreatedBy(user);
        for (Ticket ticket : userTickets) {
            repo.deleteById(ticket.getId());
        }

        userRepository.deleteById(userId);

        cacheService.evict("users:all");
        cacheService.evict("tickets:all");

        String adminUsername = SecurityUtil.currentUsername();
        auditService.log("DELETE_USER", "USER", userId, "User deleted: " + user.getUsername() + " with " + userTickets.size() + " tickets [ADMIN: " + adminUsername + "]");
    }
}
