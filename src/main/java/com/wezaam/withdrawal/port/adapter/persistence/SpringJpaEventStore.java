package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.common.domain.model.event.StoredEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaEventStore extends JpaRepository<StoredEvent, Long> {

    List<StoredEvent> findAllByOrderById();

    List<StoredEvent> findAllByIdGreaterThanOrderById(long storedEventId);
}
