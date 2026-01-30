package com.ticket.service;

import com.ticket.model.Ticket;
import com.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio para los Tickets.
 * Implementa el patrón Service Layer para separar la lógica de negocio del
 * controlador.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    /**
     * Obtiene todos los tickets del sistema.
     * 
     * @return Lista de todos los tickets
     */
    public List<Ticket> getAllTickets() {
        log.info("Obteniendo todos los tickets");
        return ticketRepository.findAll();
    }

    /**
     * Obtiene un ticket por su ID.
     * 
     * @param id ID del ticket
     * @return Optional con el ticket si existe
     */
    public Optional<Ticket> getTicketById(Long id) {
        log.info("Buscando ticket con ID: {}", id);
        return ticketRepository.findById(id);
    }

    /**
     * Crea un nuevo ticket en el sistema.
     * 
     * @param ticket Ticket a crear
     * @return Ticket creado con su ID asignado
     */
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        log.info("Creando nuevo ticket: {}", ticket.getTitulo());
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket creado exitosamente con ID: {}", savedTicket.getId());
        return savedTicket;
    }

    /**
     * Actualiza un ticket existente.
     * 
     * @param id            ID del ticket a actualizar
     * @param ticketDetails Detalles actualizados del ticket
     * @return Optional con el ticket actualizado si existe
     */
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

    /**
     * Elimina un ticket del sistema.
     * 
     * @param id ID del ticket a eliminar
     * @return true si se eliminó, false si no existía
     */
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
