package ru.hamming.event_processor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hamming.event_processor.boundary.response.IncidentResponse;
import ru.hamming.event_processor.domain.entity.IncidentEntity;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface IncidentMapper {

    @Mapping(target = "events", source = "events")
    IncidentResponse toIncidentResponse(IncidentEntity incidentEntity);
}
