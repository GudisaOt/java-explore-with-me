package ru.practicum.main_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_models.ViewStats;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatClientMainSrc {
    private final StatsClient statsClient;

    public EventFullDto viewsForEventFullDto(EventFullDto eventFullDto) {
        Set<String> uri = new HashSet<>();
        uri.add("/events/" + eventFullDto.getId());
        List<ViewStats> views = statsClient.getStatsList(uri);
        if (!views.isEmpty()) {
            eventFullDto.setViews(views.get(0).getHits());
        }
        return eventFullDto;
    }

    public List<EventFullDto> viewsForEventFullDtoList(List<EventFullDto> eventFullDtos) {
        List<Long> ids = eventFullDtos
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        Map<Long, Long> views = getViews(ids);
        if (!views.isEmpty()) {
            eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return eventFullDtos;
    }

    public List<EventShortDto> viewsForEventShortDtoList(List<EventShortDto> eventShortDtos) {
        List<Long> ids = eventShortDtos
                .stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        Map<Long, Long> views = getViews(ids);
        if (!views.isEmpty()) {
            eventShortDtos.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return eventShortDtos;
    }

    public Map<Long, Long> getViews(List<Long> ids) {
        Set<String> uri = new HashSet<>();
        for (Long id: ids) {
            uri.add("/events/" + id);
        }
        List<ViewStats> viewStats = statsClient.getStatsList(uri);
        Map<Long,Long> views = new HashMap<>();
        if (!views.isEmpty()) {
            for (ViewStats view : viewStats) {
                String[] eventUrl = view.getUri().split("/");
                views.put(Long.parseLong(eventUrl[2]), view.getHits());
            }
        }
        return views;
    }
}
