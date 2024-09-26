package com.wezaam.common.domain.model.notification;

import com.wezaam.common.domain.model.DomainEvent;
import java.time.Instant;

public class Notification<T extends DomainEvent> {

    private long id;
    private String typeName;
    private Instant occurredOn;
    private T event;

    protected Notification() {}

    public Notification(long id, T event) {
        this.setId(id);
        this.setTypeName(event.getClass().getName());
        this.setOccurredOn(event.getOccurredOn());
        this.setEvent(event);
    }

    protected void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    protected void setTypeName(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException("The typeName should not be null.");
        }

        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    protected void setOccurredOn(Instant occurredOn) {
        if (occurredOn == null) {
            throw new IllegalArgumentException("The occurredOn should not be null.");
        }

        this.occurredOn = occurredOn;
    }

    public Instant getOccurredOn() {
        return this.occurredOn;
    }

    protected void setEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event should not be null.");
        }

        this.event = event;
    }

    public T getEvent() {
        return this.event;
    }

    @Override
    public String toString() {
        return "Notification<" + this.getTypeName() + ">{"
                + "id=" + this.getId()
                + ", typeName=" + this.getTypeName()
                + ", occurredOn=" + this.getOccurredOn()
                + ", event=" + this.getEvent()
                + '}';
    }
}
