package com.wezaam.common.domain.model.notification;

import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "published_notification_tracker_store")
public class PublishedNotificationTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeName;
    private Long mostRecentlyPublishedNotificationId;

    protected PublishedNotificationTracker() {}

    public PublishedNotificationTracker(String typeName) {
        this.setTypeName(typeName);
    }

    public Long getId() {
        return this.id;
    }

    protected void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setMostRecentlyPublishedNotificationId(long mostRecentlyPublishedNotificationId) {
        this.mostRecentlyPublishedNotificationId = mostRecentlyPublishedNotificationId;
    }

    public Long getMostRecentlyPublishedNotificationId() {
        return this.mostRecentlyPublishedNotificationId;
    }
}
