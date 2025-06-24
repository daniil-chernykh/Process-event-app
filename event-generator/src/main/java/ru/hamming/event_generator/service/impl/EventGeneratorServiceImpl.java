package ru.hamming.event_generator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hamming.event_generator.domain.dto.EventDto;
import ru.hamming.event_generator.domain.entity.Event;
import ru.hamming.event_generator.domain.enums.EventType;
import ru.hamming.event_generator.mapper.EventMapper;
import ru.hamming.event_generator.service.EventGeneratorService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Сервис для генерации и отправки событий.
 * <p>
 * Предоставляет методы для автоматической (по расписанию) и ручной генерации событий
 * с последующей отправкой их на обработку через REST API.
 */
@Service
@RequiredArgsConstructor
public class EventGeneratorServiceImpl implements EventGeneratorService {

    private final RestTemplate restTemplate;
    private final EventMapper eventMapper;

    @Value("${processor.url}")
    private String baseUrl;

    /**
     * Автоматически генерирует и отправляет событие с фиксированной задержкой.
     * Задержка задается в конфигурации (${event.interval}).
     */
    @Scheduled(fixedDelayString = "${event.interval}")
    public void generateEvent() {
        Event event = createEvent();
        restTemplate.postForEntity(baseUrl, event, String.class);
    }

    /**
     * Генерирует и отправляет событие вручную.
     *
     * @return Отправленное событие.
     */
    public EventDto generateManualEvent() {
        Event event = createEvent();
        restTemplate.postForEntity(baseUrl, event, String.class);
        return eventMapper.eventToEventDto(event);
    }

    /**
     * Создает новое событие со случайным типом.
     *
     * @return Объект события ({@link Event}).
     */
    private Event createEvent() {
        return Event.builder()
                .id(UUID.randomUUID())
                .time(LocalDateTime.now())
                .type(EventType.values()[new Random().nextInt(EventType.values().length)])
                .build();
    }
}
