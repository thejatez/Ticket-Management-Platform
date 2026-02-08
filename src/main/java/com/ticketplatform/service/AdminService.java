package com.ticketplatform.service;

import com.ticketplatform.dto.response.TicketResponse;
import com.ticketplatform.dto.response.UserDetailResponse;

import java.util.List;

public interface AdminService {

    List<TicketResponse> getAllTickets();

    void deleteTicket(Long id);

    List<UserDetailResponse> getAllUsers();

    void deleteUser(Long userId);
}
