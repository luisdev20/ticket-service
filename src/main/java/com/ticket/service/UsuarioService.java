package com.ticket.service;

import com.ticket.model.Usuario;
import com.ticket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio para usuarios.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios del sistema.
     * 
     * @return lista de todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su email.
     * 
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * Valida que el email no esté duplicado.
     * 
     * @param usuario el usuario a crear
     * @return el usuario creado
     * @throws IllegalArgumentException si el email ya existe
     */
    public Usuario crear(Usuario usuario) {
        // Validar que el email no esté duplicado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica un usuario con email y password.
     * 
     * @param email    el email del usuario
     * @param password la contraseña del usuario
     * @return Optional con el usuario si las credenciales son válidas
     */
    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
}
