package com.wezaam.withdrawal.port.adapter.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.common.domain.model.DomainEvent;
import com.wezaam.common.domain.model.event.EventStore;
import com.wezaam.common.domain.model.event.StoredEvent;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJpaEventStoreAdapter implements EventStore {

    private final SpringJpaEventStore springJpaEventStore;
    private final ObjectMapper objectMapper;

    public SpringJpaEventStoreAdapter(SpringJpaEventStore springJpaEventStore, ObjectMapper objectMapper) {
        if (springJpaEventStore == null) {
            throw new IllegalArgumentException("The springJpaEventStore should not be null.");
        }

        if (objectMapper == null) {
            throw new IllegalArgumentException("The objectMapper should not be null.");
        }

        this.springJpaEventStore = springJpaEventStore;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<StoredEvent> getAllStoredEvents() {
        return this.springJpaEventStore.findAllByOrderById();
    }

    @Override
    public List<StoredEvent> getAllStoredEventsSince(long storedEventId) {
        return this.springJpaEventStore.findAllByIdGreaterThanOrderById(storedEventId);
    }

    @Override
    public StoredEvent append(DomainEvent domainEvent) {
        String eventSerialization;

        try {
            eventSerialization = this.objectMapper.writeValueAsString(domainEvent);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize the domain event: " + e.getMessage());
        }

        var storedEvent =
                new StoredEvent(domainEvent.getClass().getName(), domainEvent.getOccurredOn(), eventSerialization);

        return this.springJpaEventStore.save(storedEvent);
    }
}
