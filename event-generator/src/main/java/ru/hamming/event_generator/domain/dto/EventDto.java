package ru.hamming.event_generator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hamming.event_generator.domain.enums.EventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private UUID id;
    private EventType type;
    private LocalDateTime time;
}
