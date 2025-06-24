package ru.hamming.event_processor.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hamming.event_processor.domain.enums.IncidentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEntity {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private IncidentType type;

    private LocalDateTime time;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events = new ArrayList<>();
}
