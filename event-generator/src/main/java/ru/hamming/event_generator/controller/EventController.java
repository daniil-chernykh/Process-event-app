package ru.hamming.event_generator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hamming.event_generator.domain.dto.EventDto;
import ru.hamming.event_generator.service.EventGeneratorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventGeneratorService eventGeneratorService;

    @PostMapping
    public EventDto generateEventManually() {
        log.info("Received request to generate event manually");
        long startTime = System.currentTimeMillis();
        EventDto event = eventGeneratorService.generateManualEvent();
        log.info("Event generated successfully in {} ms: {}", System.currentTimeMillis() - startTime, event);
        return event;
    }
}
