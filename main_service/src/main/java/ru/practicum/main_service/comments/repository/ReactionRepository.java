package ru.practicum.main_service.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.comments.model.Reaction;
import ru.practicum.main_service.comments.model.ReactionModel;
import ru.practicum.main_service.user.model.User;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<ReactionModel, Long> {
    Integer countReactionModelByCommentIdAndStatusLike(Long commentId, Reaction status);

    Optional<ReactionModel> findByUserAndCommentIdAndAndStatus(User user, Long commentId, Reaction status);

    @Query(value = "SELECT * FROM reaction AS r " +
            "WHERE r.author_id = :userId " +
            "AND r.comment_id = :commentId " +
            "AND r.status like :status", nativeQuery = true)
    Optional<ReactionModel>  getReactionModelByAll(Long userId, Long commentId, Reaction status);

    Optional<ReactionModel> findReactionModelByUserIdAndCommentId(Long userId, Long commentId);
}
