package com.wezaam.common.domain.model.event;

import com.wezaam.common.domain.model.DomainEvent;
import java.util.List;

public interface EventStore {

    List<StoredEvent> getAllStoredEvents();

    List<StoredEvent> getAllStoredEventsSince(long storedEventId);

    StoredEvent append(DomainEvent domainEvent);
}
