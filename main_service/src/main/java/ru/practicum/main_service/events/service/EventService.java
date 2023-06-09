package ru.practicum.main_service.events.service;

import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.events.enums.TypesForSort;
import ru.practicum.main_service.events.dto.*;

import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto adminUpdate(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);



    List<EventShortDto> getAllByUser(Long userId, Integer from, Integer size);

    EventFullDto getFullEventByUser(Long userId, Long eventId);

    EventFullDto getEventForPublic(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEventsForPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Boolean onlyAvailable, TypesForSort sort, Integer from, Integer size, HttpServletRequest request);

    List<ParticipationRequestDto> getRequestsOnEventPrivate(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchEventRequestPrivate(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
