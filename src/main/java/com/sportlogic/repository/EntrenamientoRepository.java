package com.sportlogic.repository;

import com.sportlogic.model.Entrenamiento;
import com.sportlogic.model.Deportista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {
    List<Entrenamiento> findByDeportista(Deportista deportista);
}