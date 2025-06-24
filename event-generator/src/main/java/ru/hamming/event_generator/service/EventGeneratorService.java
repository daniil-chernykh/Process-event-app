package ru.hamming.event_generator.service;

import ru.hamming.event_generator.domain.dto.EventDto;

public interface EventGeneratorService {
    void generateEvent();
    EventDto generateManualEvent();
}
