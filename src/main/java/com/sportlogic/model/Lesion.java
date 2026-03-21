package com.sportlogic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "lesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoLesion; // Ej: Esguince de tobillo
    private String gravedad; // Leve, Media, Grave
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    
    @Column(length = 1000)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "deportista_id")
    private Deportista deportista;
}