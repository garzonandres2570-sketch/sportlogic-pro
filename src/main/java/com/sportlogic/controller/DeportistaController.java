package com.sportlogic.controller;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.Usuario;
import com.sportlogic.model.Entrenamiento;
import com.sportlogic.service.DeportistaService;
import com.sportlogic.repository.EntrenamientoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/deportistas") 
public class DeportistaController {

    private final DeportistaService deportistaService;
    private final EntrenamientoRepository entrenamientoRepository;

    public DeportistaController(DeportistaService deportistaService, EntrenamientoRepository entrenamientoRepository) {
        this.deportistaService = deportistaService;
        this.entrenamientoRepository = entrenamientoRepository;
    }

    // --- SECCIÓN ADMINISTRATIVA (Solo para ADMIN) ---

    @GetMapping("/admin/lista")
    public String listar(Model model) {
        model.addAttribute("deportistas", deportistaService.listarTodos());
        return "deportistas/lista";
    }

    @GetMapping("/admin/nuevo")
    public String formularioNuevo(Model model) {
        if (!model.containsAttribute("deportista")) {
            Deportista deportista = new Deportista();
            deportista.setUsuario(new Usuario());
            model.addAttribute("deportista", deportista);
        }
        return "deportistas/formulario";
    }

    @PostMapping("/admin/guardar")
    public String guardar(@ModelAttribute Deportista deportista, 
                         @RequestParam String rawPassword, 
                         RedirectAttributes redirectAttributes) {
        try {
            deportistaService.registrarDeportista(deportista, rawPassword);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador registrado con éxito");
            return "redirect:/deportistas/admin/lista";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("deportista", deportista);
            return "redirect:/deportistas/admin/nuevo";
        }
    }

    // --- SECCIÓN PERFIL Y AGENDA (Para el JUGADOR) ---

    /**
     * Versión mejorada: Detecta al usuario logueado automáticamente
     */
    @GetMapping("/mi-perfil")
    public String verMiPerfilPropio(Authentication auth, Model model) {
        // Obtenemos el username del usuario que tiene la sesión iniciada
        String username = auth.getName(); 
        
        // Buscamos al deportista vinculado a ese usuario
        // NOTA: Debes tener este método en tu DeportistaService
        Deportista deportista = deportistaService.buscarPorUsername(username);
        
        if (deportista == null) {
            return "redirect:/dashboard?error=no_perfil";
        }

        return cargarDatosPerfil(deportista, model);
    }

    /**
     * Versión por ID (Útil para que el Admin o Entrenador vean a un jugador)
     */
    @GetMapping("/perfil/{id}")
    public String verPerfilPorId(@PathVariable Long id, Model model) {
        Deportista deportista = deportistaService.buscarPorId(id);
        return cargarDatosPerfil(deportista, model);
    }

    // Método privado para no repetir código de carga de datos
    private String cargarDatosPerfil(Deportista deportista, Model model) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByDeportista(deportista);
        
        model.addAttribute("deportista", deportista);
        model.addAttribute("entrenamientos", entrenamientos);
        model.addAttribute("planes", List.of()); // Lista vacía hasta tener el repo de nutrición
        
        return "deportistas/mi-perfil";
    }

    @PostMapping("/completar-tarea/{id}")
    public String completarTarea(@PathVariable Long id, @RequestParam Long deportistaId, RedirectAttributes ra) {
        Entrenamiento ent = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        
        ent.setCompletado(true);
        entrenamientoRepository.save(ent);
        
        ra.addFlashAttribute("mensaje", "¡Buen trabajo! Tarea marcada como completada.");
        
        // Redirigimos dinámicamente al perfil del deportista
        return "redirect:/deportistas/perfil/" + deportistaId;
    }
}