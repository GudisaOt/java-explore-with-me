package ru.practicum.main_service.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        return ResponseEntity.ok(categoryService.getDtoById(catId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(categoryService.getAll(PageRequest.of(from,size)));
    }
}
