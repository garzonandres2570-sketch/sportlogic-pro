package com.sportlogic.config;

import com.sportlogic.model.Role;
import com.sportlogic.model.Usuario;
import com.sportlogic.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. CREAR ADMINISTRADOR (Si no existe)
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("sena2024"))
                    .email("garzonandres2570@gmail.com")
                    .rol(Role.ADMINISTRADOR)
                    .activo(true)
                    .build();
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario administrador creado: admin / sena2024");
        }

        // 2. CREAR MÉDICO DE PRUEBA (Si no existe)
        if (usuarioRepository.findByUsername("medico01").isEmpty()) {
            Usuario medico = Usuario.builder()
                    .username("medico01")
                    .password(passwordEncoder.encode("medico123")) // Contraseña para el doctor
                    .email("doctor@sportlogic.com")
                    .rol(Role.MEDICO)
                    .activo(true)
                    .build();
            usuarioRepository.save(medico);
            System.out.println("✅ Usuario médico creado: medico01 / medico123");
        }
        
        // 3. OPCIONAL: CREAR ENTRENADOR (Si no existe)
        if (usuarioRepository.findByUsername("profe01").isEmpty()) {
            Usuario entrenador = Usuario.builder()
                    .username("profe01")
                    .password(passwordEncoder.encode("profe123"))
                    .email("entrenador@sportlogic.com")
                    .rol(Role.ENTRENADOR)
                    .activo(true)
                    .build();
            usuarioRepository.save(entrenador);
            System.out.println("✅ Usuario entrenador creado: profe01 / profe123");
        }
    }
}