package com.wezaam.withdrawal.port.adapter.scheduling;

import com.wezaam.withdrawal.application.NotificationApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StoredEventsForwarder {

    private final NotificationApplicationService notificationApplicationService;

    public StoredEventsForwarder(NotificationApplicationService notificationApplicationService) {
        if (notificationApplicationService == null) {
            throw new IllegalArgumentException("The notificationApplicationService should not be null.");
        }

        this.notificationApplicationService = notificationApplicationService;
    }

    @Scheduled(fixedDelay = 5_000)
    public void forwardStoredEvents() {
        this.notificationApplicationService.publishNotifications();
    }
}
