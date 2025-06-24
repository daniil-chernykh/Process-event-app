package ru.hamming.event_processor.mapper;

import org.mapstruct.Mapper;
import ru.hamming.event_processor.boundary.request.EventRequest;
import ru.hamming.event_processor.boundary.response.EventResponse;
import ru.hamming.event_processor.domain.entity.EventEntity;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse toEventResponse(EventRequest eventRequest);

    EventRequest toEventRequest(EventResponse eventResponse);

    EventEntity toEventEntity(EventRequest eventRequest);
    EventEntity toEventEntity(EventResponse eventResponse);
}
