package ru.hamming.event_generator.mapper;

import org.mapstruct.Mapper;
import ru.hamming.event_generator.domain.dto.EventDto;
import ru.hamming.event_generator.domain.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto eventToEventDto(Event event);

    Event eventDtoToEvent(EventDto eventDto);


}
