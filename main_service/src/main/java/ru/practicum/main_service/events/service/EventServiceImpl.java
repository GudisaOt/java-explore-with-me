package ru.practicum.main_service.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.events.enums.EventStateAction;
import ru.practicum.main_service.events.enums.TypesForSort;
import ru.practicum.main_service.events.mapper.EventMapper;
import ru.practicum.main_service.events.mapper.LocationMapper;
import ru.practicum.main_service.events.models.Event;
import ru.practicum.main_service.events.models.Location;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.events.repository.LocationRepository;
import ru.practicum.main_service.exceptions.BadRequestException;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.main_service.events.enums.EventState.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
   // private final StatsClient statsClient;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        dateValidator(newEventDto.getEventDate());
        log.info("creating event");
        User eventOwner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found!"));
        Location location = getLocationToEvent(newEventDto.getLocation());
        Event event = eventMapper.toEvent(newEventDto,eventOwner,category,location,LocalDateTime.now(), EventState.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("upd by user");
        User eventOwner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        if (event.getInitiator().getId().equals(eventOwner.getId())) {
            throw new BadRequestException("You are not initiator!");
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new BadRequestException("You cant upd published event");
        }
        dateValidator(updateEventUserRequest.getEventDate());

        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);

        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);

        Optional.ofNullable(updateEventUserRequest.getEventDate()).ifPresent(event::setEventDate);

        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);

        Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);

        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);

        if (updateEventUserRequest.getCategory() > 0) {
            Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventUserRequest.getLocation() != null) {
            Location location = getLocationToEvent(updateEventUserRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventUserRequest.getStateAction() != null) {
            event.setState(toEventStateForUpd(updateEventUserRequest.getStateAction()));
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto adminUpdate(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("upd by admin");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));

        dateValidator(updateEventAdminRequest.getEventDate());

        if (event.getState().equals(PUBLISHED)) {
            throw new BadRequestException("You cant upd published event");
        }
        dateValidator(updateEventAdminRequest.getEventDate());

        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);

        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);

        Optional.ofNullable(updateEventAdminRequest.getEventDate()).ifPresent(event::setEventDate);

        Optional.ofNullable(updateEventAdminRequest.getPaid()).ifPresent(event::setPaid);

        Optional.ofNullable(updateEventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);

        Optional.ofNullable(updateEventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        Optional.ofNullable(updateEventAdminRequest.getTitle()).ifPresent(event::setTitle);

        if (updateEventAdminRequest.getCategory() > 0) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = getLocationToEvent(updateEventAdminRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            event.setState(toEventStateForUpd(updateEventAdminRequest.getStateAction()));
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
//        List<EventState> eventStates = states == null ? null : states
//                .stream()
//                .map(EventState::valueOf)
//                .collect(Collectors.toList());
//        return eventRepository.getEventsAdmin(users, eventStates, categories, PageRequest.of(from,size))
//                .stream()
//                .filter(event -> start != null ?
//                        event.getEventDate().isAfter(LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) :
//                        event.getEventDate().isAfter(LocalDateTime.now())
//                                && end != null ? event.getEventDate().isBefore(LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                        : event.getEventDate().isBefore(LocalDateTime.MAX))
//                .map(eventMapper::toEventFullDto)
//                .collect(Collectors.toList());
        log.info("get event list by admin");
        List<EventFullDto> events = eventRepository.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size)
                .stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        return events;
    }

    @Override
    public List<EventShortDto> getAllByUser(Long userId, Pageable pageable) {
        log.info("get event list by user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return eventRepository.findAllByInitiatorId(userId)
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getFullEventByUser(Long userId, Long eventId) {
        log.info("get event by user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new BadRequestException("You are not initiator!");
        }

        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto getEventForPublic(Long eventId, HttpServletRequest request) {
        log.info("get event for public");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        //statsClient.catchHit(request.getRequestURI(), request.getRemoteAddr());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsForPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, TypesForSort sort, Integer from, Integer size, HttpServletRequest request) {
        log.info("get event LIST for public");
        List<Event> events = eventRepository.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, from, size);

        if (events.isEmpty()) {
            return List.of();
        }

        Map<Long, Integer> limit = new HashMap<>();
        events.forEach(event -> limit.put(event.getId(), event.getParticipantLimit()));

        List<EventShortDto> eventsShortDto = events
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        if (onlyAvailable) {
            eventsShortDto = eventsShortDto
                    .stream()
                    .filter(eventShort -> (limit.get(eventShort.getId()) == 0 ||
                            limit.get(eventShort.getId()) > eventShort.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            if (sort.equals(TypesForSort.VIEWS)) {
                eventsShortDto.sort(Comparator.comparing(EventShortDto::getViews));
            } else if (sort.equals(TypesForSort.EVENT_DATE)) {
                eventsShortDto.sort(Comparator.comparing(EventShortDto::getEventDate));
            }
        }
       // statsClient.catchHit(request.getRequestURI(), request.getRemoteAddr());
        return eventsShortDto;
    }

    private void dateValidator(LocalDateTime evDate) {
        if (evDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Остлалось менее двух часов до события!");
        }
    }

    private Location getLocationToEvent(LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        Optional<Location> locationOptional = locationRepository.findByLatAndAndLon(locationDto.getLat(), locationDto.getLon());
        if (locationOptional.isPresent()) {
            return locationOptional.get();
        }
        return locationRepository.save(location);
    }

    private EventState toEventStateForUpd(EventStateAction eventStateAction) {
        switch (eventStateAction) {
            case REJECT_EVENT:
                return EventState.REJECTED;
            case PUBLISH_EVENT:
                return PUBLISHED;
            case CANCEL_REVIEW:
                return EventState.CANCELED;
            case SEND_TO_REVIEW:
                return EventState.PENDING;
        }
        return null;
    }
}
