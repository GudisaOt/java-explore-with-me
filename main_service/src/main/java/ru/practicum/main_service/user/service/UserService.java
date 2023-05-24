package ru.practicum.main_service.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main_service.user.dto.NewUserRequest;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    User getById(Long id);

    void deleteById(Long id);

    List<UserDto> getUserList(List<Long> id, Integer from, Integer size);
}
