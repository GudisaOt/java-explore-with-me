package ru.practicum.stats_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDto {
    private Long id;

    private String app;

    private String ip;

    private String uri;

    private LocalDateTime timestamp;
}
