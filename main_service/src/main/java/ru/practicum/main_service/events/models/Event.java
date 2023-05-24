package ru.practicum.main_service.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.events.enums.EventState;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false, length = 7001)
    private String description;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private int participantLimit;

    @Column(name = "confirmed_requests", nullable = false)
    @Builder.Default
    private Long confirmedRequests = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User initiator;

    @Column(nullable = false)
    private Boolean requestModeration;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

}
