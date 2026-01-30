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

/**
 * Controlador REST para gestionar los Tickets del sistema.
 * Expone endpoints para operaciones CRUD sobre tickets.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * Obtiene todos los tickets del sistema.
     * 
     * @return Lista de todos los tickets con status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        log.info("GET /api/tickets - Obteniendo todos los tickets");
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    /**
     * Obtiene un ticket específico por su ID.
     * 
     * @param id ID del ticket a buscar
     * @return Ticket encontrado con status 200 OK, o 404 Not Found si no existe
     */
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

    /**
     * Crea un nuevo ticket en el sistema.
     * Las validaciones se ejecutan automáticamente gracias a @Valid.
     * 
     * @param ticket Datos del ticket a crear (validados)
     * @return Ticket creado con status 201 Created
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        log.info("POST /api/tickets - Creando nuevo ticket: {}", ticket.getTitulo());
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    /**
     * Actualiza un ticket existente.
     * 
     * @param id     ID del ticket a actualizar
     * @param ticket Nuevos datos del ticket (validados)
     * @return Ticket actualizado con status 200 OK, o 404 Not Found si no existe
     */
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

    /**
     * Elimina un ticket del sistema.
     * 
     * @param id ID del ticket a eliminar
     * @return Status 204 No Content si se eliminó, o 404 Not Found si no existe
     */
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
