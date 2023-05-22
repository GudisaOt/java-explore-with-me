package ru.practicum.main_service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.events.models.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, CustomRepository {
    List<Event> findAllByInitiatorId(Long userId);

//    @Query(value =
//                    "SELECT * " +
//                    "FROM events AS e " +
//                    "WHERE e.category_id = ?1 " +
//                    "LIMIT 1", nativeQuery = true)
//    Event findFirstByCategory(Long catId);

//    @Query("SELECT e FROM events AS e " +
//            "WHERE ((:users) IS NULL OR e.user.id IN :users) " +
//            "AND ((:states) IS NULL OR e.state IN :states) " +
//            "AND ((:categories) IS NULL OR e.category.id IN :categories)")
//    Page<Event> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories, Pageable pageable);
//
//    @Query("SELECT e FROM events AS e " +
//            "WHERE (lower(e.annotation) like lower(concat('%', :text, '%')) " +
//            "OR lower(e.description) like lower(concat('%', :text, '%'))) " +
//            "AND ((:categoryIds) IS NULL OR e.category.id IN :categoryIds) " +
//            "AND e.paid = :paid " +
//            "AND e.state IN :state")
//    Page<Event> getEvenstPublic(String text, List<Long> categoryIds, Boolean paid, EventState state, Pageable pageable);
}
