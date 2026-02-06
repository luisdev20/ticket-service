package com.ticket.service;

import com.ticket.model.Categoria;
import com.ticket.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        logger.info("Obteniendo todas las categorías");
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        logger.info("Obteniendo categoría con ID: {}", id);
        return categoriaRepository.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        logger.info("Creando nueva categoría: {}", categoria.getNombre());

        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }

        Categoria categoriaCreada = categoriaRepository.save(categoria);
        logger.info("Categoría creada exitosamente con ID: {}", categoriaCreada.getId());
        return categoriaCreada;
    }
}
