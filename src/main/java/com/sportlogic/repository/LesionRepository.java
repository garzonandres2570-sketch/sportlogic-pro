package com.sportlogic.repository;

import com.sportlogic.model.Lesion;
import com.sportlogic.model.Deportista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LesionRepository extends JpaRepository<Lesion, Long> {
    List<Lesion> findByDeportistaOrderByFechaInicioDesc(Deportista deportista);
}