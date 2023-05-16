package ru.practicum.main_service.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main_service.compilation.mapper.CompilationMapper;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.mapper.EventMapper;
import ru.practicum.main_service.events.models.Event;
import ru.practicum.main_service.events.repository.EventRepository;
import ru.practicum.main_service.exceptions.BadRequestException;
import ru.practicum.main_service.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents().isEmpty()) {
            throw new BadRequestException("Empty events list!");
        }
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());

        Compilation compilation = compilationRepository.save(compilationMapper.newDtoToCompilation(newCompilationDto, events));

        return getById(compilation.getId());
    }

    @Override
    public CompilationDto patch(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Not found!"));

        Optional.ofNullable(updateCompilationRequest.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(updateCompilationRequest.getPinned()).ifPresent(compilation::setPinned);

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return getById(compId);
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Not found"));
        List<EventShortDto> eventShortDtos = compilation.getEvents()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compilation,eventShortDtos);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Pageable pageable) {

        return compilationRepository.findAllByPinned(pinned, pageable)
                .stream()
                .map(this::mapToCompDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Not found"));

        compilationRepository.deleteById(compId);
    }

    private CompilationDto mapToCompDto(Compilation compilation) {
        return CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents()
                        .stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .build();
    }
}
