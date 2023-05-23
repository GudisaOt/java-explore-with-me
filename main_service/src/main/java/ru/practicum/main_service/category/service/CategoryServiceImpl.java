package ru.practicum.main_service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exceptions.ConflictException;
import ru.practicum.main_service.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final EventRepository eventRepository;


    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findFirstByName(newCategoryDto.getName()) != null) {
            throw new ConflictException("Category already exist");
        }
        return categoryMapper.categoryToCategoryDto(categoryRepository
                .save(categoryMapper.newCatDtoToCategory(newCategoryDto)));
    }

    @Override
    public CategoryDto getDtoById(Long id) {
        return categoryMapper.categoryToCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!!!")));
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!!!"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!!!"));
        if (eventRepository.findFirstByCategoryId(id) != null) {
            throw new ConflictException("you cant delete a category in use");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category catToUpd = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!!!"));

        if (categoryRepository.findFirstByName(categoryDto.getName()) != null
                && !categoryRepository.findFirstByName(categoryDto.getName()).getId().equals(id)) {
            throw new ConflictException("Category already exist");
        }

        Optional.ofNullable(categoryDto.getName()).ifPresent(catToUpd::setName);
        return categoryMapper.categoryToCategoryDto(categoryRepository.save(catToUpd));
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }
}
