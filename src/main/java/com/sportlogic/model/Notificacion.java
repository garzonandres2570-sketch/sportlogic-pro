package com.sportlogic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;
    private String tipo; // "ERROR", "ADVERTENCIA", "INFO"
    private LocalDateTime fechaEnvio;
    private boolean leido = false;

    // Quien recibe la notificación (opcional: puedes usar el nombre de usuario o el Rol)
    private String destinatarioRol; 
}