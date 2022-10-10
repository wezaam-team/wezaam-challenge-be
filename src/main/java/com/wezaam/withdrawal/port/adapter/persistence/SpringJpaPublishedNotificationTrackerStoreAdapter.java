package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.common.domain.model.notification.PublishedNotificationTracker;
import com.wezaam.common.domain.model.notification.PublishedNotificationTrackerStore;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJpaPublishedNotificationTrackerStoreAdapter implements PublishedNotificationTrackerStore {

    private final SpringJpaPublishedNotificationTrackerStore springJpaPublishedNotificationTrackerStore;

    public SpringJpaPublishedNotificationTrackerStoreAdapter(
            SpringJpaPublishedNotificationTrackerStore springJpaPublishedNotificationTrackerStore) {

        if (springJpaPublishedNotificationTrackerStore == null) {
            throw new IllegalArgumentException("The springJpaPublishedNotificationTrackerStore should not be null.");
        }

        this.springJpaPublishedNotificationTrackerStore = springJpaPublishedNotificationTrackerStore;
    }

    @Override
    public PublishedNotificationTracker getPublishedNotificationTrackerOfTypeName(String typeName) {
        return this.springJpaPublishedNotificationTrackerStore.findByTypeName(typeName);
    }

    @Override
    public PublishedNotificationTracker save(PublishedNotificationTracker publishedNotificationTracker) {
        return this.springJpaPublishedNotificationTrackerStore.save(publishedNotificationTracker);
    }
}
