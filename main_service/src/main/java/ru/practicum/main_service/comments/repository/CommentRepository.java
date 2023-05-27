package ru.practicum.main_service.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.comments.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(Long id);

    List<Comment> findAllByEventId(Long id);
}