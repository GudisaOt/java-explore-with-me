package ru.practicum.main_service.user.service;

import ru.practicum.main_service.user.dto.NewUserRequest;
import ru.practicum.main_service.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    UserDto getById(Long id);

    void deleteById(Long id);

    List<UserDto> getUserList(List<Long> id, Integer from, Integer size);
}
