package com.sportlogic.controller;

import com.sportlogic.repository.DeportistaRepository;
import com.sportlogic.repository.NotificacionRepository;
import com.sportlogic.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final DeportistaRepository deportistaRepository;
    private final NotificacionRepository notificacionRepository;
    
    @Autowired
    private ReporteService reporteService;

    public LoginController(DeportistaRepository deportistaRepository, NotificacionRepository notificacionRepository) {
        this.deportistaRepository = deportistaRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        long totalDeportistas = deportistaRepository.count();
        long lesionados = deportistaRepository.findAll().stream()
                .filter(d -> "Lesionado".equalsIgnoreCase(d.getEstadoSalud()))
                .count();

        model.addAttribute("totalDeportistas", totalDeportistas);
        model.addAttribute("totalLesionados", lesionados);

        model.addAttribute("porteros", deportistaRepository.findAll().stream().filter(d -> "Portero".equalsIgnoreCase(d.getPosicion())).count());
        model.addAttribute("defensas", deportistaRepository.findAll().stream().filter(d -> "Defensa".equalsIgnoreCase(d.getPosicion())).count());
        model.addAttribute("volantes", deportistaRepository.findAll().stream().filter(d -> "Volante".equalsIgnoreCase(d.getPosicion())).count());
        model.addAttribute("delanteros", deportistaRepository.findAll().stream().filter(d -> "Delantero".equalsIgnoreCase(d.getPosicion())).count());

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENTRENADOR"))) {
            model.addAttribute("alertas", notificacionRepository.findByDestinatarioRolAndLeidoFalseOrderByFechaEnvioDesc("ROLE_ENTRENADOR"));
        }

        return "dashboard";
    }

    @GetMapping("/admin/reporte/pdf")
    public ResponseEntity<byte[]> descargarReporte() {
        byte[] pdf = reporteService.generarReportePlantilla();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Reporte_TormentaCeleste.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
}