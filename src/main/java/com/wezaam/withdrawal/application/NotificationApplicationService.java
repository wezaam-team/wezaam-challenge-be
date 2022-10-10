package com.wezaam.withdrawal.application;

import com.wezaam.common.domain.model.notification.NotificationPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationApplicationService {

    private final NotificationPublisher notificationPublisher;

    public NotificationApplicationService(NotificationPublisher notificationPublisher) {
        if (notificationPublisher == null) {
            throw new IllegalArgumentException(" The notificationPublisher should not be null.");
        }

        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public void publishNotifications() {
        this.notificationPublisher.publishNotifications();

    }
}
