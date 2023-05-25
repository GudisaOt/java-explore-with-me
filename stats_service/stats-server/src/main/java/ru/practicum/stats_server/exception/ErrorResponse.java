package ru.practicum.stats_server.exception;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final String reason;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final String timestamp;

    public ErrorResponse(HttpStatus status, String message, String reason, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.reason = reason;
        this.timestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
