package com.ticket.controller;

import com.ticket.model.Categoria;
import com.ticket.service.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        logger.info("GET /api/categorias - Obteniendo todas las categorías");
        List<Categoria> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        logger.info("GET /api/categorias/{} - Obteniendo categoría", id);
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Categoria categoria) {
        logger.info("POST /api/categorias - Creando nueva categoría: {}", categoria.getNombre());

        try {
            Categoria categoriaCreada = categoriaService.crear(categoria);
            logger.info("Categoría creada exitosamente con ID: {}", categoriaCreada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear categoría: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
