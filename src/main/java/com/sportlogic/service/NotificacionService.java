package com.sportlogic.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final JavaMailSender mailSender;

    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarAlerta(String titulo, String mensaje, String rolDestino) {
        // Usa tu correo real aquí para la prueba de Tormenta Celeste FC
        String correoDestino = "garzonandres2570@gmail.com"; 

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("garzonandres2570@gmail.com"); // IMPORTANTE: El remitente debe ser el mismo del username
            email.setTo(correoDestino);
            email.setSubject("[SportLogic Pro] " + titulo);
            email.setText(mensaje + "\n\nEste es un mensaje automático de Tormenta Celeste FC.");
            
            mailSender.send(email);
            System.out.println("✅ Notificación enviada con éxito a " + correoDestino);
        } catch (Exception e) {
            System.err.println("❌ ERROR ENVIANDO CORREO: " + e.getMessage());
            // Esto imprimirá el error real en la consola para saber por qué fallan los 9 intentos
            e.printStackTrace(); 
        }
    }
}