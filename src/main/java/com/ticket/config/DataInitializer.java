package com.ticket.config;

import com.ticket.model.Rol;
import com.ticket.model.Usuario;
import com.ticket.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente que inicializa datos de prueba en la base de datos.
 * Se ejecuta automáticamente al iniciar la aplicación.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método que se ejecuta después de la construcción del bean.
     * Crea un usuario de prueba si no existe.
     */
    @PostConstruct
    public void init() {
        logger.info("Inicializando datos de prueba...");

        // Verificar si ya existe el usuario admin
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
