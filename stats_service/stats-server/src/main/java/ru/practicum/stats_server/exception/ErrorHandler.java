package ru.practicum.stats_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse badRequestHandle(BadRequest badRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                "BAD REQUEST",
                badRequest.getMessage(),
                LocalDateTime.now());
    }
}
