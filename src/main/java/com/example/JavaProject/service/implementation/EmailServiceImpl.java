package com.example.JavaProject.service.implementation;

import com.example.JavaProject.service.interfaces.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendLikeNotification(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Ktoś polubił Twój przepis");
        message.setText("Ktoś właśnie polubił Twój przepis. Sprawdź, kto to jest!");

        mailSender.send(message);
    }
}
