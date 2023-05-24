package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_models.EndpointHit;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class StatsClientController {

    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> catchHit(
            @RequestBody EndpointHit endpointHit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statsClient.post("/hit",endpointHit));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "uris", required = false) Set<String> uris) {
        return statsClient.get("/stats", params, uris);
    }
}
