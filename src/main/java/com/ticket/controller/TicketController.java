package com.ticket.controller;

import com.ticket.model.Ticket;
import com.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        log.info("GET /api/tickets - Obteniendo todos los tickets");
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        log.info("GET /api/tickets/{} - Buscando ticket", id);
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Ticket con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        log.info("POST /api/tickets - Creando nuevo ticket: {}", ticket.getTitulo());
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody Ticket ticket) {
        log.info("PUT /api/tickets/{} - Actualizando ticket", id);
        return ticketService.updateTicket(id, ticket)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("No se puede actualizar. Ticket con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.info("DELETE /api/tickets/{} - Eliminando ticket", id);
        boolean deleted = ticketService.deleteTicket(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            log.warn("No se puede eliminar. Ticket con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }
}
