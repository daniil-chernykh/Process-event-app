package ru.hamming.event_processor.repository;

import org.springframework.data.repository.CrudRepository;
import ru.hamming.event_processor.domain.entity.EventEntity;

import java.util.UUID;

public interface EventRepository extends CrudRepository<EventEntity, UUID> {
}
