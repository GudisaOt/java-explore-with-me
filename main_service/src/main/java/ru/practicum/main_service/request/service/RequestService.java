package ru.practicum.main_service.request.service;

import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long evId);

    ParticipationRequestDto cancel(Long requestId, Long userId);

    List<ParticipationRequestDto> getAll(Long userId);
}
