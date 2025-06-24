package ru.hamming.event_processor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.domain.dto.IncidentDto;

public interface EventProcessorService {
    void processEvent(EventRequest event);
    Page<IncidentResponse> getIncidents(PageRequest pageRequest);
}
