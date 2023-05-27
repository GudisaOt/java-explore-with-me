package ru.practicum.main_service.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.comments.model.Reaction;
import ru.practicum.main_service.comments.model.ReactionModel;


import java.util.Optional;

public interface ReactionRepository extends JpaRepository<ReactionModel, Long> {
    Integer countReactionModelByCommentIdAndStatusLike(Long commentId, Reaction status);

    Optional<ReactionModel> findReactionModelByUserIdAndCommentId(Long userId, Long commentId);
}
