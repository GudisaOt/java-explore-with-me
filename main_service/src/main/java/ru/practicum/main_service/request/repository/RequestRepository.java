package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long reqId);

    Optional<Request> findByRequesterIdAndEventId(Long reqId, Long evId);

    List<Request> findAllByEventId(Long eventId);

    int countRequestsByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByIdInAndStatus(List<Long> ids, RequestStatus status);
}
