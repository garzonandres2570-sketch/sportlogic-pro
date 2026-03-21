package com.sportlogic.controller;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.PlanNutricional;
import com.sportlogic.model.Lesion;
import com.sportlogic.repository.DeportistaRepository;
import com.sportlogic.repository.PlanNutricionalRepository;
import com.sportlogic.repository.LesionRepository;
import com.sportlogic.service.NotificacionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/medico")
@PreAuthorize("hasRole('MEDICO')")
public class MedicoController {

    private final DeportistaRepository deportistaRepository;
    private final PlanNutricionalRepository planNutricionalRepository;
    private final LesionRepository lesionRepository;
    private final NotificacionService notificacionService;

    public MedicoController(DeportistaRepository deportistaRepository, 
                            PlanNutricionalRepository planNutricionalRepository,
                            LesionRepository lesionRepository,
                            NotificacionService notificacionService) {
        this.deportistaRepository = deportistaRepository;
        this.planNutricionalRepository = planNutricionalRepository;
        this.lesionRepository = lesionRepository;
        this.notificacionService = notificacionService;
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        model.addAttribute("deportistas", deportistaRepository.findAll());
        return "medico/lista-pacientes";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam Long id, @RequestParam String nuevoEstado) {
        Optional<Deportista> opt = deportistaRepository.findById(id);
        if (opt.isPresent()) {
            Deportista d = opt.get();
            d.setEstadoSalud(nuevoEstado);
            deportistaRepository.save(d);

            if ("Lesionado".equalsIgnoreCase(nuevoEstado) || "Fatiga".equalsIgnoreCase(nuevoEstado)) {
                String mensaje = "Atención: El estado de salud de " + d.getNombreCompleto() + 
                                 " ha cambiado a: " + nuevoEstado;
                notificacionService.enviarAlerta("CAMBIO DE ESTADO MÉDICO", mensaje, "ROLE_ENTRENADOR");
            }
        }
        return "redirect:/medico/pacientes?updated";
    }

    @GetMapping("/asignar-plan/{id}")
    public String formularioPlan(@PathVariable Long id, Model model) {
        Deportista deportista = deportistaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deportista no encontrado"));
        model.addAttribute("deportista", deportista);
        model.addAttribute("nuevoPlan", new PlanNutricional());
        return "medico/form-nutricion";
    }

    @PostMapping("/guardar-plan")
    public String guardarPlan(@ModelAttribute PlanNutricional plan, @RequestParam Long deportistaId) {
        deportistaRepository.findById(deportistaId).ifPresent(dep -> {
            plan.setDeportista(dep);
            plan.setFechaAsignacion(LocalDate.now());
            planNutricionalRepository.save(plan);
        });
        return "redirect:/medico/pacientes?success=plan_asignado";
    }

    @GetMapping("/historial/{id}")
    public String verHistorial(@PathVariable Long id, Model model) {
        Deportista dep = deportistaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deportista no encontrado"));
        model.addAttribute("deportista", dep);
        model.addAttribute("lesiones", lesionRepository.findByDeportistaOrderByFechaInicioDesc(dep));
        model.addAttribute("nuevaLesion", new Lesion());
        return "medico/historial-lesiones";
    }

    @PostMapping("/guardar-lesion")
    public String guardarLesion(@ModelAttribute Lesion lesion, @RequestParam Long deportistaId) {
        deportistaRepository.findById(deportistaId).ifPresent(dep -> {
            lesion.setDeportista(dep);
            lesion.setFechaInicio(LocalDate.now());
            lesionRepository.save(lesion);
            
            dep.setEstadoSalud("Lesionado");
            deportistaRepository.save(dep);

            // CORRECCIÓN: Se usa getObservaciones() en lugar de getDescripcion()
            String aviso = "⚠️ ALERTA MÉDICA - TORMENTA CELESTE FC\n\n" +
                           "El deportista " + dep.getNombreCompleto() + " ha registrado una lesión.\n" +
                           "Tipo: " + lesion.getTipoLesion() + "\n" +
                           "Observaciones: " + (lesion.getObservaciones() != null ? lesion.getObservaciones() : "Sin detalles") + "\n" +
                           "Fecha de reporte: " + LocalDate.now();
            
            notificacionService.enviarAlerta("REPORTE DE LESIÓN", aviso, "ROLE_ENTRENADOR");
        });
        return "redirect:/medico/historial/" + deportistaId + "?success";
    }
}