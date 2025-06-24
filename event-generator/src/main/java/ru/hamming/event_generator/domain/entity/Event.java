package ru.hamming.event_generator.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hamming.event_generator.domain.enums.EventType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, представляющий событие.
 * <p>
 * Содержит информацию об идентификаторе, типе и времени создания события.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private UUID id;
    private EventType type;
    private LocalDateTime time;
}
