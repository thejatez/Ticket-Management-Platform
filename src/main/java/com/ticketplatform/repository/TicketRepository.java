package com.ticketplatform.repository;

import com.ticketplatform.entity.Ticket;
import com.ticketplatform.entity.User;
import com.ticketplatform.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreatedBy(User user);

    List<Ticket> findByStatus(TicketStatus status);
}
