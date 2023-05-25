package ru.practicum.main_service.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    @NotBlank
    private String title;
    private Boolean pinned = false;
    private List<Long> events = new ArrayList<>();
}
