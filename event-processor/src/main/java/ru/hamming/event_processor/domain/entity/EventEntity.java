package ru.hamming.event_processor.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hamming.event_processor.domain.enums.EventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private IncidentEntity incident;
}
