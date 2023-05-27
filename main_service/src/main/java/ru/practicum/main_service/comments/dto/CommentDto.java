package ru.practicum.main_service.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
    private String eventTitle;
    private int positive;
    private int negative;
}
