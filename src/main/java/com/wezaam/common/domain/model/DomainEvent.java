package com.wezaam.common.domain.model;

import java.time.Instant;

public interface DomainEvent {

    Instant getOccurredOn();
}
