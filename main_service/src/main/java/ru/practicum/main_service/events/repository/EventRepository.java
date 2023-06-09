package ru.practicum.main_service.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.events.models.Event;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, CustomRepository {

    Page<Event> findEventsByInitiatorId(Long userId, PageRequest pageRequest);

    Optional<Event> findFirstByCategoryId(Long catId);
}
