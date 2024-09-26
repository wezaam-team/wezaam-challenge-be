package com.wezaam.common.domain.model.notification;

public interface PublishedNotificationTrackerStore {

    PublishedNotificationTracker getPublishedNotificationTrackerOfTypeName(String typeName);

    PublishedNotificationTracker save(PublishedNotificationTracker publishedNotificationTracker);
}
