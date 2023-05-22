package ru.practicum.stats_server.service;

import org.springframework.stereotype.Service;
import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;
import ru.practicum.stats_server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {
    Stats catchHit(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
