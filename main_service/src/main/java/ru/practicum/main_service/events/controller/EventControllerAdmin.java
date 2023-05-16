package ru.practicum.main_service.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam(required = false) List<Long> users,
                                                        @RequestParam(required = false) List<EventState> states,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                        @PositiveOrZero @RequestParam (required = false, defaultValue = "0")  Integer from,
                                                        @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long eventId,
                                                   @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return ResponseEntity.ok(eventService.adminUpdate(eventId, updateEventAdminRequest));
    }
}
