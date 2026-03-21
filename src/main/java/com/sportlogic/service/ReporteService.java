package com.sportlogic.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.sportlogic.model.Deportista;
import com.sportlogic.repository.DeportistaRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReporteService {

    private final DeportistaRepository repository;

    public ReporteService(DeportistaRepository repository) {
        this.repository = repository;
    }

    public byte[] generarReportePlantilla() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título principal
            document.add(new Paragraph("TORMENTA CELESTE FC - REPORTE OFICIAL")
                    .setBold().setFontSize(18));
            document.add(new Paragraph("Listado Maestro de Deportistas - SportLogic Pro"));
            document.add(new Paragraph(" "));

            // Configuración de la tabla
            float[] columnWidths = {4, 3, 3}; // Proporción de las columnas
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.useAllAvailableWidth();

            // Encabezados de tabla
            table.addHeaderCell(new Paragraph("Nombre Completo").setBold());
            table.addHeaderCell(new Paragraph("Posición").setBold());
            table.addHeaderCell(new Paragraph("Estado Salud").setBold());

            // Obtener datos de la base de datos
            List<Deportista> lista = repository.findAll();

            for (Deportista d : lista) {
                // IMPORTANTE: Se usa getNombreCompleto() porque así está en tu Deportista.java
                table.addCell(d.getNombreCompleto() != null ? d.getNombreCompleto() : "N/A");
                table.addCell(d.getPosicion() != null ? d.getPosicion() : "Sin asignar");
                table.addCell(d.getEstadoSalud() != null ? d.getEstadoSalud() : "Desconocido");
            }

            document.add(table);
            document.close();
            
        } catch (Exception e) {
            // Log del error para depuración
            System.err.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        }
        
        return baos.toByteArray();
    }
}