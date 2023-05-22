package ru.practicum.main_service.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.dto.NewUserRequest;
import ru.practicum.main_service.user.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUserFromDto(UserDto userDto);

    UserShortDto toUserShort(User user);

    User toUserFromReq(NewUserRequest newUserRequest);
}
