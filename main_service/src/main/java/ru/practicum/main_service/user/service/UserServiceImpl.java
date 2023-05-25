package ru.practicum.main_service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.exceptions.ConflictException;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.user.dto.NewUserRequest;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserDto createUser(NewUserRequest newUserRequest) {
        if (newUserRequest.getName() != null) {
            if (userRepository.findFirstByName(newUserRequest.getName()).isPresent()) {
                throw new ConflictException("Username already exist!!!");
            }
        }

        return userMapper.toUserDto(userRepository.save(userMapper.toUserFromReq(newUserRequest)));
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!!!")));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUserList(List<Long> id, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (id == null) {
            return userRepository.findAll(pageRequest)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        if (id.size() > 0) {
            List<UserDto> i = userRepository.getAll(id, pageRequest)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
            return userRepository.getAll(id, pageRequest)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageRequest)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}
