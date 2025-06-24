package ru.hamming.event_processor.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.service.EventProcessorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventProcessorController {
    private final EventProcessorService service;

    @PostMapping("/events")
    @Operation(summary = "Process incoming event")
    public void processEvent(@RequestBody EventRequest event) {
        log.info("Received event to process: {}", event);
        long startTime = System.currentTimeMillis();
        service.processEvent(event);
        log.info("Event processed successfully in {} ms", System.currentTimeMillis() - startTime);
    }

    @GetMapping("/incidents")
    @Operation(summary = "Get list of incidents with pagination and sorting")
    public Page<IncidentResponse> getIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "time") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        log.info("Received request for incidents: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);
        long startTime = System.currentTimeMillis();
        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<IncidentResponse> incidents = service.getIncidents(PageRequest.of(page, size, sort));
        log.info("Returned {} incidents in {} ms", incidents.getTotalElements(), System.currentTimeMillis() - startTime);
        return incidents;
    }
}
