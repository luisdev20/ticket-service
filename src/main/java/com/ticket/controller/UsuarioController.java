package com.ticket.controller;

import com.ticket.dto.LoginRequest;
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

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        logger.info("Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        logger.info("Obteniendo usuario con ID: {}", id);
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
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

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Intento de login para: {}", request.getEmail());

        try {
            var usuario = usuarioService.login(request.getEmail(), request.getPassword());

            if (usuario.isPresent()) {
                logger.info("Login exitoso para: {}", request.getEmail());
                return ResponseEntity.ok(usuario.get());
            } else {
                logger.warn("Login fallido para: {} - Credenciales inválidas", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales inválidas");
            }
        } catch (Exception e) {
            logger.error("Error en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar login");
        }
    }
}
