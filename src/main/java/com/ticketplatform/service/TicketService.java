package com.ticketplatform.service;

import com.ticketplatform.dto.request.TicketCreateRequest;
import com.ticketplatform.dto.request.TicketUpdateRequest;
import com.ticketplatform.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(TicketCreateRequest request);

    List<TicketResponse> getMyTickets();

    void updateTicket(Long id, TicketUpdateRequest request);

    void closeTicket(Long id);

    void reopenTicket(Long id);

    void deleteTicket(Long id);
}
