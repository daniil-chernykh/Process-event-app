package ru.hamming.event_processor.boundary.response;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponse {
    private UUID id;
    private IncidentType type;
    private LocalDateTime time;
    private List<EventResponse> events = new ArrayList<>();
}
