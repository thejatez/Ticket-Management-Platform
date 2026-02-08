package com.ticketplatform.controller;

import com.ticketplatform.dto.request.TicketCreateRequest;
import com.ticketplatform.dto.request.TicketUpdateRequest;
import com.ticketplatform.dto.response.TicketResponse;
import com.ticketplatform.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketCreateRequest request) {
        return ticketService.createTicket(request);
    }

    @GetMapping("/my")
    public List<TicketResponse> myTickets() {
        return ticketService.getMyTickets();
    }

    @PutMapping("/{id}")
    public void updateTicket(@PathVariable Long id,
                             @RequestBody TicketUpdateRequest request) {
        ticketService.updateTicket(id, request);
    }

    @PutMapping("/{id}/close")
    public void closeTicket(@PathVariable Long id) {
        ticketService.closeTicket(id);
    }

    @PutMapping("/{id}/reopen")
    public void reopenTicket(@PathVariable Long id) {
        ticketService.reopenTicket(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
