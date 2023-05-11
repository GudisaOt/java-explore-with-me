package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_models.models.EndpointHit;
import ru.practicum.stats_models.models.ViewStats;
import ru.practicum.stats_server.model.Stats;
import ru.practicum.stats_server.model.StatsMapper;
import ru.practicum.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public Stats catchHit(EndpointHit hit) {
        return statsRepository.save(statsMapper.toStats(hit, LocalDateTime.parse(hit.getTimestamp(), formatter)));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.findAllStatsUriDist(start, end);
            } else {
                return statsRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.findStatsByUrisDist(start, end, uris);
            } else {
                return statsRepository.findStatsByUris(start, end, uris);
            }
        }
    }
}
