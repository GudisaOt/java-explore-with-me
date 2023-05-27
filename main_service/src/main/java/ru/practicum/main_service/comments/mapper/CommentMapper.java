package ru.practicum.main_service.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "eventTitle", source = "comment.event.title")
    CommentDto toCommentDto(Comment comment);
}
