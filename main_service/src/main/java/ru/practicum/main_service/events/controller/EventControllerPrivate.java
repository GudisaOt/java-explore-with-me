package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.dto.UpdateEventUserRequest;
import ru.practicum.main_service.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEvents(@PathVariable Long userId,
                                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.getAllByUser(userId, PageRequest.of(from / size, size)));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId,
                                               @Valid @RequestBody NewEventDto newEventDto) {
        return ResponseEntity.ok(eventService.create(userId,newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getFullEventByUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patch(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return ResponseEntity.ok(eventService.update(userId, eventId, updateEventUserRequest));
    }

//    @GetMapping("/{eventId}/requests")
//    public ResponseEntity<List<ParticipationRequestDto>> getEventRequestsByEventOwner(@PathVariable Long userId,
//                                                                                      @PathVariable Long eventId) {
//        return requestService.getEventRequestsByEventOwner(userId, eventId);
//    }
//
//    @PatchMapping("/{eventId}/requests")
//    @ResponseStatus(HttpStatus.OK)
//    public EventRequestStatusUpdateResult patchEventRequestsByEventOwner(
//            @PathVariable Long userId,
//            @PathVariable Long eventId,
//            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
//        return requestService.patchEventRequestsByEventOwner(userId, eventId, eventRequestStatusUpdateRequest);
//    }
}
