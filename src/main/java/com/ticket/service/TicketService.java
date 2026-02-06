package com.ticket.service;

import com.ticket.model.Ticket;
import com.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    
    public List<Ticket> getAllTickets() {
        log.info("Obteniendo todos los tickets");
        return ticketRepository.findAll();
    }

    
    public Optional<Ticket> getTicketById(Long id) {
        log.info("Buscando ticket con ID: {}", id);
        return ticketRepository.findById(id);
    }

    
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        log.info("Creando nuevo ticket: {}", ticket.getTitulo());
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket creado exitosamente con ID: {}", savedTicket.getId());
        return savedTicket;
    }

    
    @Transactional
    public Optional<Ticket> updateTicket(Long id, Ticket ticketDetails) {
        log.info("Actualizando ticket con ID: {}", id);
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTitulo(ticketDetails.getTitulo());
                    ticket.setDescripcion(ticketDetails.getDescripcion());
                    ticket.setPrioridad(ticketDetails.getPrioridad());
                    ticket.setEstado(ticketDetails.getEstado());
                    Ticket updated = ticketRepository.save(ticket);
                    log.info("Ticket actualizado exitosamente: {}", id);
                    return updated;
                });
    }

    
    @Transactional
    public boolean deleteTicket(Long id) {
        log.info("Eliminando ticket con ID: {}", id);
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            log.info("Ticket eliminado exitosamente: {}", id);
            return true;
        }
        log.warn("No se encontró el ticket con ID: {}", id);
        return false;
    }
}
