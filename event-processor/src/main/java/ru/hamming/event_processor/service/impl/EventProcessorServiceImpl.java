package ru.hamming.event_processor.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.EventResponse;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.domain.dto.IncidentDto;
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
        EventEntity eventEntity = eventMapper.toEventEntity(event);
        eventRepository.save(eventEntity);

        if (event.getType() == EventType.TYPE_1) {
            boolean matched = false;
            for (EventResponse pendingEvent : new ArrayList<>(pendingType2Events)) {
                if (pendingEvent.getTime().isAfter(event.getTime().minusSeconds(20))) {
                    createIncident(IncidentType.TYPE_2, List.of(pendingEvent, eventMapper.toEventResponse(event)));
                    pendingType2Events.remove(pendingEvent);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                createIncident(IncidentType.TYPE_1, List.of(eventMapper.toEventResponse(event)));
            }
        } else if (event.getType() == EventType.TYPE_2) {
            pendingType2Events.add(eventMapper.toEventResponse(event));
            pendingType2Events.removeIf(e -> e.getTime().isBefore(LocalDateTime.now().minusSeconds(20)));
        }
    }

    @Override
    public Page<IncidentResponse> getIncidents(PageRequest pageRequest) {
        Page<IncidentEntity> incidentPage = incidentRepository.findAllWithEvents(pageRequest);
        List<IncidentResponse> incidentResponses = incidentPage.getContent().stream()
                .map(incidentMapper::toIncidentResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(incidentResponses, pageRequest, incidentPage.getTotalElements());
    }

    private void createIncident(IncidentType type, List<EventResponse> events) {
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
    }
}
