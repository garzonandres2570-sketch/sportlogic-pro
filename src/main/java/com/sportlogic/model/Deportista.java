package com.sportlogic.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deportistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String posicion;        // HU001: Delantero, Defensa, etc.
    private String estadoSalud;     // HU034: Normal, Lesionado, Fatiga.
    
    @Column(length = 2000)
    private String rendimiento;     // HU002: Seguimiento de rendimiento.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}