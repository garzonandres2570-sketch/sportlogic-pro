package com.sportlogic.repository;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.Usuario; // Importante importar Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DeportistaRepository extends JpaRepository<Deportista, Long> {
    
    // Este es el que te pide el error actual:
    Optional<Deportista> findByUsuario(Usuario usuario);

    // Este es el que usamos en el paso anterior para buscar por texto:
    Optional<Deportista> findByUsuarioUsername(String username);
}