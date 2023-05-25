package ru.practicum.main_service.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;
import ru.practicum.main_service.category.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto getDtoById(Long id);

    void delete(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    List<CategoryDto> getAll(Pageable pageable);


}
