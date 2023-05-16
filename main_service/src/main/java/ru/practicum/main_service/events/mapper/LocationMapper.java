package ru.practicum.main_service.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.events.dto.LocationDto;
import ru.practicum.main_service.events.models.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "id", expression = "java(null)")
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);
}
