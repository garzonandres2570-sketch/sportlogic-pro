package com.sportlogic.repository;

import com.sportlogic.model.PlanNutricional;
import com.sportlogic.model.Deportista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanNutricionalRepository extends JpaRepository<PlanNutricional, Long> {
    List<PlanNutricional> findByDeportista(Deportista deportista);
}