package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.events.models.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exceptions.BadRequestException;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long evId) {
        log.info("create request");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!!!"));
        Event event = eventRepository.findById(evId)
                .orElseThrow(() -> new NotFoundException("Event not found!!!"));

        if (event.getInitiator().getEmail().equals(user.getEmail())) {
            throw new BadRequestException("Initiator cant create a request!");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new BadRequestException("Event is not published");
        }
        if (event.getParticipantLimit() != 0 || event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new BadRequestException("Event is full");
        }
        Optional<Request> checkRepeat = requestRepository.findByRequesterIdAndEventId(userId, evId);
        if (checkRepeat.isPresent()) {
            throw new BadRequestException("Request already exists");
        }

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status((event.getRequestModeration()) ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();
        return requestMapper.toPartReqDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancel(Long requestId, Long userId) {
        log.info("CANCEL REQUEST");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!!!"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found!"));
        if (request.getRequester().getId().equals(userId)) {
            throw new BadRequestException("User is not requester!");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toPartReqDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAll(Long userId) {
        log.info("get all REQWUEST");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!!!"));
        List<ParticipationRequestDto> list = requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(requestMapper::toPartReqDto)
                .collect(Collectors.toList());
        return list;
    }
}
