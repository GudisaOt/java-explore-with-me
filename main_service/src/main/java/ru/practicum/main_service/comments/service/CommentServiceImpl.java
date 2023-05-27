package ru.practicum.main_service.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.PostComment;
import ru.practicum.main_service.comments.mapper.CommentMapper;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.comments.model.Reaction;
import ru.practicum.main_service.comments.model.ReactionModel;
import ru.practicum.main_service.comments.repository.CommentRepository;
import ru.practicum.main_service.comments.repository.ReactionRepository;
import ru.practicum.main_service.events.models.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exceptions.BadRequestException;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    @Override
    public CommentDto createComment(Long userId, Long eventId, PostComment postComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        Comment comment = Comment.builder()
                .author(user)
                .event(event)
                .text(postComment.getText())
                .created(LocalDateTime.now())
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("Only author can delete comment!");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void adminDelete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto patchComment(Long userId, Long commentId, PostComment postComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("Only author can patch comment!");
        }
        comment.setText(postComment.getText());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllUserComments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return commentRepository.findAllByAuthorId(userId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addReaction(Long userId, Long commentId, Reaction reaction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        Optional<ReactionModel> r = reactionRepository.findReactionModelByUserIdAndCommentId(userId,commentId);

        if (r.isPresent()) {
            throw new BadRequestException("Вы не можете повторно ставить оценку комментарию");
        }
            reactionRepository.save(
                    ReactionModel.builder()
                            .status(reaction)
                            .comment(comment)
                            .user(user)
                            .build()
            );
            if (reaction.equals(Reaction.POSITIVE)) {
                comment.setPositive(
                         reactionRepository.countReactionModelByCommentIdAndStatusLike(commentId,reaction)
                );
            } else {
                comment.setNegative(
                        reactionRepository.countReactionModelByCommentIdAndStatusLike(commentId, reaction)
                );
            }
            commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
