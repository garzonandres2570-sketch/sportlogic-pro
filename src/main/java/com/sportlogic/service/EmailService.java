package com.sportlogic.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarNotificacionLesion(String destinatario, String nombreJugador, String estado) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("SportLogic Pro <tu-correo@gmail.com>");
        message.setTo(destinatario);
        message.setSubject("⚠️ ALERTA MÉDICA: " + nombreJugador);
        message.setText("Se ha actualizado el estado de salud en SportLogic Pro.\n\n" +
                "Jugador: " + nombreJugador + "\n" +
                "Nuevo Estado: " + estado + "\n\n" +
                "Por favor, revisa el dashboard de Tormenta Celeste FC para más detalles.");
        
        mailSender.send(message);
        System.out.println("Correo enviado con éxito a " + destinatario);
    }
}