package ru.practicum.main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.compilation.service.CompilationService;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.create(newCompilationDto));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patch(@PathVariable Long compId,
                                                @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return ResponseEntity.ok(compilationService.patch(compId, updateCompilationRequest));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long compId) {
        compilationService.deleteById(compId);
        return ResponseEntity.noContent().build();
    }
}
