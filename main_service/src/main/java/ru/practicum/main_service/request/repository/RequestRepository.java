package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long reqId);

    Optional<Request> findByRequesterIdAndEventId(Long reqId, Long evId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByEventIdAndStatus(Long eventId, RequestStatus status);

    int countRequestsByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByIdInAndStatus(List<Long> ids, RequestStatus status);

//    @Query("SELECT new ru.practicum.main_service.events.dto.RequestStats(r.event.id, count(r.id)) " +
//            "FROM Request AS r " +
//            "WHERE r.event.id IN ?1 " +
//            "AND r.status = 'CONFIRMED' " +
//            "GROUP BY r.event.id")
//    List<RequestStats> getConfirmedRequests(List<Long> eventsId);
}
