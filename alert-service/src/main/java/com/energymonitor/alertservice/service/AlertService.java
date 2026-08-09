package com.energymonitor.alertservice.service;

import com.energymonitor.alertservice.notification.EmailService;
import com.energymonitor.common.events.AlertingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.energymonitor.common.events.StaticEventNames.ALERT_USAGE_TOPIC;
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final EmailService emailService;

    @KafkaListener(topics = ALERT_USAGE_TOPIC)
    public void energyUsageAlertEvent(AlertingEvent alertingEvent) {
        log.info("Received alert event: {}", alertingEvent);

        final String subject = "Energy Usage Alert for User "
                + alertingEvent.userId();
        final String message = "Alert: " + alertingEvent.message() +
                "\nThreshold: " + alertingEvent.threshold() +
                "\nEnergy Consumed: " + alertingEvent.energyConsumed();
        emailService.sendEmail(alertingEvent.email(),
                subject,
                message,
                alertingEvent.userId());
    }
}
