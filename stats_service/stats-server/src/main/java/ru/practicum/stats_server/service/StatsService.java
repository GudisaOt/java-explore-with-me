package ru.practicum.stats_server.service;


import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;
import ru.practicum.stats_server.model.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsDto catchHit(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
