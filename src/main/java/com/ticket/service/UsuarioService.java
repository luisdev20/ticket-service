package com.ticket.service;

import com.ticket.model.Usuario;
import com.ticket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    
    public Usuario crear(Usuario usuario) {
        // Validar que el email no esté duplicado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }

    
    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
}
