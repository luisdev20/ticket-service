package com.ticket.repository;

import com.ticket.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona métodos CRUD y consultas personalizadas.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email.
     * 
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado.
     * 
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por email y password para autenticación.
     * 
     * @param email    el email del usuario
     * @param password la contraseña del usuario
     * @return Optional con el usuario si las credenciales son válidas
     */
    Optional<Usuario> findByEmailAndPassword(String email, String password);
}
