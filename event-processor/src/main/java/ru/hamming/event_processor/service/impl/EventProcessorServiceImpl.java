package ru.hamming.event_processor.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.EventResponse;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.domain.entity.EventEntity;
import ru.hamming.event_processor.domain.entity.IncidentEntity;
import ru.hamming.event_processor.domain.enums.EventType;
import ru.hamming.event_processor.domain.enums.IncidentType;
import ru.hamming.event_processor.mapper.EventMapper;
import ru.hamming.event_processor.mapper.IncidentMapper;
import ru.hamming.event_processor.repository.EventRepository;
import ru.hamming.event_processor.repository.IncidentRepository;
import ru.hamming.event_processor.service.EventProcessorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventProcessorServiceImpl implements EventProcessorService {

    private final IncidentRepository incidentRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final IncidentMapper incidentMapper;
    private final List<EventResponse> pendingType2Events = new ArrayList<>();

    @Override
    public void processEvent(EventRequest event) {
        log.info("Processing event: {}", event);
        EventEntity eventEntity = eventMapper.toEventEntity(event);
        eventRepository.save(eventEntity);

        if (event.getType() == EventType.TYPE_1) {
            log.debug("Checking TYPE_1 event against pending TYPE_2 events");
            boolean matched = false;
            for (EventResponse pendingEvent : new ArrayList<>(pendingType2Events)) {
                if (pendingEvent.getTime().isAfter(event.getTime().minusSeconds(20))) {
                    log.info("Match found, creating TYPE_2 incident");
                    createIncident(IncidentType.TYPE_2, List.of(pendingEvent, eventMapper.toEventResponse(event)));
                    pendingType2Events.remove(pendingEvent);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                log.info("No match, creating TYPE_1 incident");
                createIncident(IncidentType.TYPE_1, List.of(eventMapper.toEventResponse(event)));
            }
        } else if (event.getType() == EventType.TYPE_2) {
            log.info("Adding TYPE_2 event to pending list: {}", event);
            pendingType2Events.add(eventMapper.toEventResponse(event));
            pendingType2Events.removeIf(e -> e.getTime().isBefore(LocalDateTime.now().minusSeconds(20)));
            log.debug("Pending TYPE_2 events count: {}", pendingType2Events.size());
        }
    }

    @Override
    public Page<IncidentResponse> getIncidents(PageRequest pageRequest) {
        log.info("Fetching incidents with pageRequest: {}", pageRequest);
        Page<IncidentEntity> incidentPage = incidentRepository.findAllWithEvents(pageRequest);
        List<IncidentResponse> incidentResponses = incidentPage.getContent().stream()
                .map(incidentMapper::toIncidentResponse)
                .collect(Collectors.toList());
        log.debug("Mapped {} incidents to responses", incidentResponses.size());
        return new PageImpl<>(incidentResponses, pageRequest, incidentPage.getTotalElements());
    }

    private void createIncident(IncidentType type, List<EventResponse> events) {
        log.debug("Creating incident of type: {}", type);
        List<EventEntity> eventEntities = events.stream()
                .map(eventMapper::toEventEntity)
                .map(eventRepository::save)
                .collect(Collectors.toList());

        IncidentEntity incident = IncidentEntity.builder()
                .id(UUID.randomUUID())
                .type(type)
                .time(LocalDateTime.now())
                .events(eventEntities)
                .build();

        incidentRepository.save(incident);
        log.info("Incident created with ID: {}", incident.getId());
    }
}
