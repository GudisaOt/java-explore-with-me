package ru.practicum.main_service.events.repository;

import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.events.models.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate conjunction = builder.conjunction();

        if (users != null && !users.isEmpty()) {
            conjunction = builder.and(conjunction, root.get("initiator").in(users));
        }

        if (states != null && !states.isEmpty()) {
            conjunction = builder.and(conjunction, root.get("state").in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            conjunction = builder.and(conjunction, root.get("category").in(categories));
        }

        if (rangeStart != null) {
            conjunction = builder.and(conjunction, builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            conjunction = builder.and(conjunction, builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        query.select(root).where(conjunction);
        return entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();
    }

    @Override
    public List<Event> getEventsByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Integer from, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate conjunction = builder.conjunction();

        if (text != null && !text.isBlank()) {
            Predicate annotation = builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate description = builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
            conjunction = builder.and(conjunction, builder.or(annotation, description));
        }

        if (categories != null && !categories.isEmpty()) {
            conjunction = builder.and(conjunction, root.get("category").in(categories));
        }

        if (paid != null) {
            conjunction = builder.and(conjunction, root.get("paid").in(paid));
        }

        if (rangeStart == null && rangeEnd == null) {
            conjunction = builder.and(conjunction, builder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));
        } else {
            if (rangeStart != null) {
                conjunction = builder.and(conjunction, builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }

            if (rangeEnd != null) {
                conjunction = builder.and(conjunction, builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
        }

        conjunction = builder.and(conjunction, root.get("state").in(EventState.PUBLISHED));

        query.select(root).where(conjunction);
        return entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();
    }
}
