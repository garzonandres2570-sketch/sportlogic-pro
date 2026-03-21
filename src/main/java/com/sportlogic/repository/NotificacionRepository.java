package com.sportlogic.repository;

import com.sportlogic.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Para que el entrenador solo vea sus alertas sin leer
    List<Notificacion> findByDestinatarioRolAndLeidoFalseOrderByFechaEnvioDesc(String rol);
}