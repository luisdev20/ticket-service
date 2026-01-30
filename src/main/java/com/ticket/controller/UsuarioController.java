package com.ticket.controller;

import com.ticket.model.Usuario;
import com.ticket.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar usuarios.
 * Proporciona endpoints para crear y listar usuarios.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios.
     * 
     * @return lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        logger.info("Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id el ID del usuario
     * @return el usuario encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        logger.info("Obteniendo usuario con ID: {}", id);
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario.
     * 
     * @param usuario el usuario a crear
     * @return el usuario creado con status 201
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario) {
        logger.info("Creando nuevo usuario: {}", usuario.getEmail());

        try {
            Usuario usuarioCreado = usuarioService.crear(usuario);
            logger.info("Usuario creado exitosamente con ID: {}", usuarioCreado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
