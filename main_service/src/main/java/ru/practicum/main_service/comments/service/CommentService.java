package ru.practicum.main_service.comments.service;

import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.PostComment;
import ru.practicum.main_service.comments.model.Reaction;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, PostComment postComment);

    void deleteComment(Long userId, Long commentId);

    void adminDelete(Long commentId);

    CommentDto patchComment(Long userId, Long commentId, PostComment postComment);

    CommentDto getComment(Long userId, Long commentId);

    List<CommentDto> getAllUserComments(Long userId);

    CommentDto addReaction(Long userId, Long commentId, Reaction reaction);
}
