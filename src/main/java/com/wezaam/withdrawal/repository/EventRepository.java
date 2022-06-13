package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
