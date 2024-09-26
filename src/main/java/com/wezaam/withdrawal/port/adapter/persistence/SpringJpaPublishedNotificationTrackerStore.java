package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.common.domain.model.notification.PublishedNotificationTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaPublishedNotificationTrackerStore extends JpaRepository<PublishedNotificationTracker, Long> {

    PublishedNotificationTracker findByTypeName(String typeName);
}
