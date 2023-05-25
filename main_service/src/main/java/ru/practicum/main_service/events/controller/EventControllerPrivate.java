package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.getAllByUser(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId,
                                               @Valid @RequestBody NewEventDto newEventDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(userId,newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getFullEventByUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patch(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return ResponseEntity.ok(eventService.update(userId, eventId, updateEventUserRequest));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsOnEventByUser(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getRequestsOnEventPrivate(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> processWithEventsRequests(
            @Valid @RequestBody EventRequestStatusUpdateRequest requests,
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.patchEventRequestPrivate(userId, eventId, requests));
    }
}
