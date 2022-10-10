package com.wezaam.common.domain.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.common.domain.model.DomainEvent;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "event_store")
public class StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Instant occurredOn;

    @Column(length = 65_000)
    private String body;

    protected StoredEvent() {}

    public StoredEvent(String type, Instant occurredOn, String body) {
        this.setType(type);
        this.setOccurredOn(occurredOn);
        this.setBody(body);
    }

    public Long getId() {
        return this.id;
    }

    protected void setType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("The type should not be null.");
        }

        this.type = type;
    }

    public String getType() {
        return this.type;
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

    protected void setBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException("The body should not be null.");
        }

        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> T toDomainEvent(ObjectMapper objectMapper) {
        Class<T> domainEventClass;

        try {
            domainEventClass = (Class<T>) Class.forName(this.getType());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class of name: " + this.getType() + " not found.");
        }

        T domainEvent;
        try {
            domainEvent = objectMapper.readValue(this.getBody(), domainEventClass);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not deserialize the event body: " + e.getMessage());
        }

        return domainEvent;
    }
}
