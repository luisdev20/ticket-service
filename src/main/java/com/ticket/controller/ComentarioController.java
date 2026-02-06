package com.ticket.controller;

import com.ticket.model.Comentario;
import com.ticket.service.ComentarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class ComentarioController {

    private static final Logger logger = LoggerFactory.getLogger(ComentarioController.class);

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/{ticketId}/comentarios")
    public ResponseEntity<List<Comentario>> obtenerComentarios(@PathVariable Long ticketId) {
        logger.info("GET /api/tickets/{}/comentarios - Obteniendo comentarios", ticketId);
        List<Comentario> comentarios = comentarioService.obtenerPorTicket(ticketId);
        return ResponseEntity.ok(comentarios);
    }

    @PostMapping("/{ticketId}/comentarios")
    public ResponseEntity<?> crearComentario(
            @PathVariable Long ticketId,
            @RequestBody Map<String, Object> body) {

        logger.info("POST /api/tickets/{}/comentarios - Creando comentario", ticketId);

        try {
            String texto = (String) body.get("texto");
            Long usuarioId = ((Number) body.get("usuarioId")).longValue();

            if (texto == null || texto.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El texto del comentario es obligatorio");
            }

            Comentario comentario = comentarioService.crear(ticketId, usuarioId, texto);
            logger.info("Comentario creado exitosamente con ID: {}", comentario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear comentario: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear comentario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear comentario");
        }
    }
}
