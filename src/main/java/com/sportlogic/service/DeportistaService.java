package com.sportlogic.service;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.Role;
import com.sportlogic.model.Usuario;
import com.sportlogic.repository.DeportistaRepository;
import com.sportlogic.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeportistaService {

    private final DeportistaRepository deportistaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DeportistaService(DeportistaRepository deportistaRepository, 
                            UsuarioRepository usuarioRepository, 
                            PasswordEncoder passwordEncoder) {
        this.deportistaRepository = deportistaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- MÉTODOS EXISTENTES ---

    @Transactional
    public void registrarDeportista(Deportista deportista, String password) {
        // Validar duplicados antes de procesar
        if (usuarioRepository.existsByEmail(deportista.getUsuario().getEmail())) {
            throw new RuntimeException("Error: El correo electrónico ya está registrado.");
        }
        if (usuarioRepository.existsByUsername(deportista.getUsuario().getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
        }

        Usuario user = deportista.getUsuario();
        user.setPassword(passwordEncoder.encode(password));
        user.setRol(Role.DEPORTISTA);
        user.setActivo(true);
        
        usuarioRepository.save(user);
        deportistaRepository.save(deportista);
    }

    public List<Deportista> listarTodos() {
        return deportistaRepository.findAll();
    }

    // --- MÉTODOS NUEVOS (Para corregir los errores de compilación) ---

    /**
     * Busca un deportista por su ID único.
     */
    public Deportista buscarPorId(Long id) {
        return deportistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el deportista con ID: " + id));
    }

    /**
     * Busca al deportista asociado a un nombre de usuario específico.
     * Útil para el botón "Mi Perfil" automático.
     */
    public Deportista buscarPorUsername(String username) {
        return deportistaRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: No existe un perfil de deportista para el usuario: " + username));
    }
}