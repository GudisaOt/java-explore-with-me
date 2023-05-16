package ru.practicum.main_service.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;

import java.util.List;


public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto patch(Long compId, UpdateCompilationRequest updateCompilationRequest);

    CompilationDto getById(Long compId);

    List<CompilationDto> getAll(Boolean pinned, Pageable pageable);

    void deleteById(Long compId);
}
