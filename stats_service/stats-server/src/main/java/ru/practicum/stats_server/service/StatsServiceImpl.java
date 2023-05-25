package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;
import ru.practicum.stats_server.exception.BadRequest;
import ru.practicum.stats_server.model.StatsDto;
import ru.practicum.stats_server.model.StatsMapper;
import ru.practicum.stats_server.repository.StatsRepository;


import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    private final StatsMapper statsMapper;


    @Override
    @Transactional
    public StatsDto catchHit(EndpointHit hit) {
        return statsMapper.toStatsDto(statsRepository.save(statsMapper.toStats(hit, hit.getTimestamp())));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (end.isBefore(start)) {
            throw new BadRequest("Date is incorrect");
        }
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
