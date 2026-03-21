package com.sportlogic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrenamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private LocalDateTime fechaHora; 
    private String lugar;
    private String imagenUrl; // <--- NUEVO CAMPO PARA FOTOS PRO

    @Builder.Default
    private boolean completado = false; 

    @ManyToOne
    @JoinColumn(name = "deportista_id")
    private Deportista deportista;
}