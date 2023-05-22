package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.dto.UpdateEventUserRequest;
import ru.practicum.main_service.events.service.EventService;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Slf4j
@Validated
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEvents(@PathVariable Long userId,
                                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("PRIVATE CONTROLLER get all");
        return ResponseEntity.ok(eventService.getAllByUser(userId, PageRequest.of(from / size, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                                               @Valid @RequestBody NewEventDto newEventDto) {
        log.info("PRIVATE CONTROLLER create");
        return eventService.create(userId,newEventDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        log.info("PRIVATE CONTROLLER get event");
        return ResponseEntity.ok(eventService.getFullEventByUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patch(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PRIVATE CONTROLLER patch");
        return ResponseEntity.ok(eventService.update(userId, eventId, updateEventUserRequest));
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsOnEventByUser(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return eventService.getRequestsOnEventPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult processWithEventsRequests(
            @Valid @RequestBody EventRequestStatusUpdateRequest requests,
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return eventService.patchEventRequestPrivate(userId, eventId, requests);
    }
}
