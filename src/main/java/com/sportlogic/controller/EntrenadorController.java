package com.sportlogic.controller;

import com.sportlogic.model.Deportista;
import com.sportlogic.model.Entrenamiento;
import com.sportlogic.repository.DeportistaRepository;
import com.sportlogic.repository.EntrenamientoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/entrenador")
public class EntrenadorController {

    private final EntrenamientoRepository entrenamientoRepository;
    private final DeportistaRepository deportistaRepository;

    public EntrenadorController(EntrenamientoRepository entrenamientoRepository, DeportistaRepository deportistaRepository) {
        this.entrenamientoRepository = entrenamientoRepository;
        this.deportistaRepository = deportistaRepository;
    }

    @GetMapping("/asignar")
    public String formularioAsignar(Model model) {
        model.addAttribute("entrenamiento", new Entrenamiento());
        model.addAttribute("deportistas", deportistaRepository.findAll());
        return "entrenador/asignar-tarea";
    }

    @PostMapping("/guardar")
    public String guardarEntrenamiento(@ModelAttribute Entrenamiento entrenamiento, RedirectAttributes redirectAttributes) {
        // Si no se proporciona imagen, asignamos una deportiva profesional por defecto
        if (entrenamiento.getImagenUrl() == null || entrenamiento.getImagenUrl().trim().isEmpty()) {
            entrenamiento.setImagenUrl("https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=1000");
        }
        
        entrenamiento.setCompletado(false);
        entrenamientoRepository.save(entrenamiento);
        
        redirectAttributes.addFlashAttribute("mensaje", "¡Actividad programada y publicada con éxito!");
        return "redirect:/entrenador/seguimiento";
    }

    @GetMapping("/seguimiento")
    public String verSeguimiento(Model model) {
        model.addAttribute("deportistas", deportistaRepository.findAll());
        model.addAttribute("entrenamientoRepo", entrenamientoRepository);
        return "entrenador/seguimiento";
    }

    @GetMapping("/rendimiento/{id}")
    public String verRendimiento(@PathVariable Long id, 
                                @RequestParam(required = false) String temporada, 
                                Model model) {
        
        Deportista deportista = deportistaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deportista no encontrado"));

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByDeportista(deportista);

        if (temporada != null && !temporada.isEmpty()) {
            int anioBusqueda = Integer.parseInt(temporada);
            entrenamientos = entrenamientos.stream()
                .filter(e -> e.getFechaHora().getYear() == anioBusqueda)
                .collect(Collectors.toList());
        }

        model.addAttribute("deportista", deportista);
        model.addAttribute("entrenamientos", entrenamientos);
        model.addAttribute("tieneDatos", !entrenamientos.isEmpty()); 
        model.addAttribute("temporadaFiltro", temporada);

        return "entrenador/rendimiento-detalle";
    }

    @GetMapping("/rendimiento/exportar/{id}")
    public void exportarPDF(@PathVariable Long id, 
                            @RequestParam(required = false) String temporada, 
                            HttpServletResponse response) throws IOException {
        
        long startTime = System.currentTimeMillis();
        Deportista deportista = deportistaRepository.findById(id).orElseThrow();
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByDeportista(deportista);

        if (temporada != null && !temporada.isEmpty()) {
            int anio = Integer.parseInt(temporada);
            entrenamientos = entrenamientos.stream()
                .filter(e -> e.getFechaHora().getYear() == anio)
                .collect(Collectors.toList());
        }

        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=Reporte_" + deportista.getNombreCompleto().replace(" ", "_") + ".pdf";
        response.setHeader("Content-Disposition", headerValue);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        document.add(new Paragraph("REPORTE DE RENDIMIENTO - SPORTLOGIC PRO", titleFont));
        document.add(new Paragraph("Jugador: " + deportista.getNombreCompleto()));
        document.add(new Paragraph("Temporada: " + (temporada != null ? temporada : "Todas")));
        document.add(new Paragraph(" "));

        if (entrenamientos.isEmpty()) {
            document.add(new Paragraph("No hay datos registrados para este periodo."));
        } else {
            for (Entrenamiento e : entrenamientos) {
                document.add(new Paragraph("- " + e.getTitulo() + " | Fecha: " + e.getFechaHora().toLocalDate()));
            }
        }

        document.close();
        System.out.println("PDF Generado en: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}