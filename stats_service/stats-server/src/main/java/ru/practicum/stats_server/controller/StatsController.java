package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;
import ru.practicum.stats_server.model.StatsDto;
import ru.practicum.stats_server.service.StatsService;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        LocalDateTime st = LocalDateTime.parse(start, formatter);
        LocalDateTime en = LocalDateTime.parse(end, formatter);
        return statsService.getStats(st, en, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<StatsDto> catchHit(@Valid @RequestBody EndpointHit endpointHit) {

        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.catchHit(endpointHit));
    }

}
