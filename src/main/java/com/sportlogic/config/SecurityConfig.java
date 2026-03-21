package com.sportlogic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación profesional
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. DESACTIVAR CSRF (Obligatorio para que Postman pueda hacer POST/PUT)
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
                // Archivos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() 
                
                // 2. RUTA LIBRE PARA PRUEBAS (Para que Postman no te pida Login)
                .requestMatchers("/api/**").permitAll() 
                
                // Seguridad por roles para la interfaz web
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/medico/**").hasRole("MEDICO")
                .requestMatchers("/entrenador/**").hasRole("ENTRENADOR")
                .requestMatchers("/deportista/**").hasRole("DEPORTISTA")
                
                // Todo lo demás sigue requiriendo autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}