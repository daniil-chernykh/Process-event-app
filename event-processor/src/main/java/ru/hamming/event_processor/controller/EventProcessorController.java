package ru.hamming.event_processor.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.service.EventProcessorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventProcessorController {
    private final EventProcessorService service;

    @PostMapping("/events")
    @Operation(summary = "Process incoming event")
    public void processEvent(@RequestBody EventRequest event) {
        service.processEvent(event);
    }

    @GetMapping("/incidents")
    @Operation(summary = "Get list of incidents with pagination and sorting")
    public Page<IncidentResponse> getIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "time") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return service.getIncidents(PageRequest.of(page, size, sort));
    }
}
