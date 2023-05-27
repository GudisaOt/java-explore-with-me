package ru.practicum.main_service.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.dto.PostComment;
import ru.practicum.main_service.comments.model.Reaction;
import ru.practicum.main_service.comments.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(@PathVariable Long userId, @RequestParam Long eventId,
                                             @Valid @RequestBody PostComment postComment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(userId, eventId, postComment));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> patch(@PathVariable Long userId, @PathVariable Long commentId,
                                             @Valid @RequestBody PostComment postComment) {
        return ResponseEntity.ok().body(commentService.patchComment(userId, commentId, postComment));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return ResponseEntity.ok().body(commentService.getComment(userId, commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable Long userId) {
        return ResponseEntity.ok(commentService.getAllUserComments(userId));
    }

    @PatchMapping("/react/{commentId}")
    public ResponseEntity<CommentDto> addReaction(@PathVariable Long userId, @PathVariable Long commentId,
                                                  @RequestParam Reaction reaction) {
        return ResponseEntity.ok(commentService.addReaction(userId, commentId, reaction));
    }
}
