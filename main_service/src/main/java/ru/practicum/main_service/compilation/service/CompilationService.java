package ru.practicum.main_service.compilation.service;

import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;

import java.util.List;


public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto patch(Long compId, UpdateCompilationRequest updateCompilationRequest);

    CompilationDto getById(Long compId);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    void deleteById(Long compId);
}
