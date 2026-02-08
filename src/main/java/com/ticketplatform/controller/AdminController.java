package com.ticketplatform.controller;

import com.ticketplatform.dto.response.TicketResponse;
import com.ticketplatform.dto.response.UserDetailResponse;
import com.ticketplatform.service.AdminService;
import com.ticketplatform.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final TicketService ticketService;

    public AdminController(AdminService adminService, TicketService ticketService) {
        this.adminService = adminService;
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getAllTickets() {
        return adminService.getAllTickets();
    }

    @GetMapping("/users")
    public List<UserDetailResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/tickets/{id}")
    public void deleteTicket(@PathVariable Long id) {
        adminService.deleteTicket(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
}
