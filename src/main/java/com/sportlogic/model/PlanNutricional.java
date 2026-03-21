package com.sportlogic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "planes_nutricionales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanNutricional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo; // Ej: Dieta Hiperproteica
    
    @Column(length = 1000)
    private String descripcion; // Detalle de la dieta
    
    private String recomendaciones; // Ej: Hidratarse 3L al día
    private LocalDate fechaAsignacion;

    @ManyToOne
    @JoinColumn(name = "deportista_id")
    private Deportista deportista;
}