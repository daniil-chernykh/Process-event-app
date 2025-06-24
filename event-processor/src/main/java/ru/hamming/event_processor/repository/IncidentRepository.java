package ru.hamming.event_processor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hamming.event_processor.domain.entity.IncidentEntity;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IncidentRepository extends CrudRepository<IncidentEntity, UUID> {
    @Query("SELECT i FROM IncidentEntity i LEFT JOIN FETCH i.events")
    Page<IncidentEntity> findAllWithEvents(Pageable pageable);
}
