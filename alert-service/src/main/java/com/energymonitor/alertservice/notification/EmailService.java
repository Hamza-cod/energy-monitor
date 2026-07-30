package com.energymonitor.alertservice.notification;

import com.energymonitor.alertservice.entity.Alert;
import com.energymonitor.alertservice.repository.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final AlertRepository alertRepository;

    public EmailService(JavaMailSender mailSender,
                        AlertRepository alertRepository) {
        this.mailSender = mailSender;
        this.alertRepository = alertRepository;
    }

    public void sendEmail(String to,
                          String subject,
                          String body,
                          String userId) {
        log.info("Sending email to: {}, subject: {}", to, subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("noreply@energymonitor.com");
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);

            final Alert alertSent = Alert.builder()
                    .sent(true)
                    .createdAt(Instant.now())
                    .userId(userId)
                    .build();

            alertRepository.saveAndFlush(alertSent);

        } catch (MailException e) {
            log.error("Failed to send email to: {}", to, e);

            final Alert alertSent = Alert.builder()
                    .sent(false)
                    .createdAt(Instant.now())
                    .userId(userId)
                    .build();
            alertRepository.saveAndFlush(alertSent);
            return;
        }

        log.info("Email sent to: {}", to);
    }
}
