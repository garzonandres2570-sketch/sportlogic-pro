package com.sportlogic.controller;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.Entrenamiento;
import com.sportlogic.model.Usuario;
import com.sportlogic.model.PlanNutricional;
import com.sportlogic.repository.DeportistaRepository;
import com.sportlogic.repository.UsuarioRepository;
import com.sportlogic.repository.EntrenamientoRepository;
import com.sportlogic.repository.PlanNutricionalRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/deportista")
public class PerfilDeportistaController {

    private final DeportistaRepository deportistaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntrenamientoRepository entrenamientoRepository;
    private final PlanNutricionalRepository planNutricionalRepository; // Añadido

    public PerfilDeportistaController(DeportistaRepository deportistaRepository, 
                                    UsuarioRepository usuarioRepository,
                                    EntrenamientoRepository entrenamientoRepository,
                                    PlanNutricionalRepository planNutricionalRepository) {
        this.deportistaRepository = deportistaRepository;
        this.usuarioRepository = usuarioRepository;
        this.entrenamientoRepository = entrenamientoRepository;
        this.planNutricionalRepository = planNutricionalRepository;
    }

    @GetMapping("/perfil")
    public String verMiPerfil(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
        
        if (usuarioOpt.isPresent()) {
            Optional<Deportista> deportistaOpt = deportistaRepository.findByUsuario(usuarioOpt.get());
            
            if (deportistaOpt.isPresent()) {
                Deportista deportista = deportistaOpt.get();
                model.addAttribute("deportista", deportista);
                
                // Cargar Entrenamientos
                List<Entrenamiento> entrenamientos = entrenamientoRepository.findByDeportista(deportista);
                model.addAttribute("entrenamientos", entrenamientos);
                
                // NUEVO: Cargar Planes Nutricionales
                List<PlanNutricional> planes = planNutricionalRepository.findByDeportista(deportista);
                model.addAttribute("planes", planes);
                
                return "deportistas/mi-perfil"; 
            }
        }
        return "redirect:/dashboard?error=perfil_no_encontrado";
    }

    @PostMapping("/completar/{id}")
    public String completarEntrenamiento(@PathVariable Long id) {
        Optional<Entrenamiento> entOpt = entrenamientoRepository.findById(id);
        if (entOpt.isPresent()) {
            Entrenamiento ent = entOpt.get();
            ent.setCompletado(true);
            entrenamientoRepository.save(ent);
        }
        return "redirect:/deportista/perfil?success=completado";
    }
}