package ru.hamming.event_generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hamming.event_generator.domain.dto.EventDto;
import ru.hamming.event_generator.service.EventGeneratorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventGeneratorService eventGeneratorService;

    @PostMapping
    public EventDto generateEventManually() {
        return eventGeneratorService.generateManualEvent();
    }
}
