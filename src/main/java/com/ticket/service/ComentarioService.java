package com.ticket.service;

import com.ticket.model.Comentario;
import com.ticket.model.Ticket;
import com.ticket.model.Usuario;
import com.ticket.repository.ComentarioRepository;
import com.ticket.repository.TicketRepository;
import com.ticket.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    private static final Logger logger = LoggerFactory.getLogger(ComentarioService.class);

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Comentario> obtenerPorTicket(Long ticketId) {
        logger.info("Obteniendo comentarios del ticket ID: {}", ticketId);
        return comentarioRepository.findByTicketIdOrderByFechaAsc(ticketId);
    }

    public Comentario crear(Long ticketId, Long usuarioId, String texto) {
        logger.info("Creando comentario en ticket ID: {} por usuario ID: {}", ticketId, usuarioId);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado con ID: " + ticketId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        Comentario comentario = new Comentario();
        comentario.setTexto(texto);
        comentario.setTicket(ticket);
        comentario.setUsuario(usuario);

        Comentario comentarioCreado = comentarioRepository.save(comentario);
        logger.info("Comentario creado exitosamente con ID: {}", comentarioCreado.getId());
        return comentarioCreado;
    }
}
