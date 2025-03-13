package com.demo.springkafkaevent.outboxmessagerelay;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventOutboxRepository extends JpaRepository<EventOutboxEntity, Long> {
    List<EventOutboxEntity> findAllByCreatedAtLessThanEqualOrderByCreatedAtAsc(LocalDateTime localDateTime, Pageable pageable);
}
