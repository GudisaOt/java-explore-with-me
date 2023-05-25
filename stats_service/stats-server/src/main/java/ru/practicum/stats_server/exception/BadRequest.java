package ru.practicum.stats_server.exception;

public class BadRequest extends RuntimeException {
    public BadRequest(final String message) {
        super(message);
    }
}
