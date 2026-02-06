package com.ticket.config;

import com.ticket.model.Categoria;
import com.ticket.model.Rol;
import com.ticket.model.Usuario;
import com.ticket.repository.CategoriaRepository;
import com.ticket.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostConstruct
    public void init() {
        logger.info("Inicializando datos de prueba...");

        initCategorias();
        initUsuario();
    }

    private void initCategorias() {
        String[] categorias = { "Hardware", "Software", "Redes" };

        for (String nombreCategoria : categorias) {
            if (!categoriaRepository.existsByNombre(nombreCategoria)) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombreCategoria);
                categoriaRepository.save(categoria);
                logger.info("✅ Categoría creada: {}", nombreCategoria);
            } else {
                logger.info("ℹ️ Categoría ya existe: {}", nombreCategoria);
            }
        }
    }

    private void initUsuario() {
        if (!usuarioRepository.existsByEmail("admin@test.com")) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@test.com");
            admin.setPassword("1234");
            admin.setRol(Rol.TECNICO);

            usuarioRepository.save(admin);
            logger.info("✅ Usuario de prueba creado: admin@test.com / 1234");
        } else {
            logger.info("ℹ️ Usuario de prueba ya existe: admin@test.com");
        }
    }
}
