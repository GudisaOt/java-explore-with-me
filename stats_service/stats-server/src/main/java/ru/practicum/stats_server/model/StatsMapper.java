package ru.practicum.stats_server.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats_models.EndpointHit;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mapping(target = "timestamp", expression = "java(timestamp)")
    Stats toStats(EndpointHit hit, LocalDateTime timestamp);

    StatsDto toStatsDto(Stats stats);
}
