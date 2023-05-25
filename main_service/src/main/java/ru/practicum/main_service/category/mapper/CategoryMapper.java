package ru.practicum.main_service.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryDtoToCategory(CategoryDto categoryDto);

    Category newCatDtoToCategory(NewCategoryDto newCategoryDto);

    CategoryDto categoryToCategoryDto(Category category);
}
