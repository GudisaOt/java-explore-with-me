package ru.practicum.main_service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.exceptions.ConflictException;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.user.dto.NewUserRequest;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.model.User;
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
            if (userRepository.findFirstByName(newUserRequest.getName()) != null) {
                throw new ConflictException("Username already exist!!!");
            }
        }

        return userMapper.toUserDto(userRepository.save(userMapper.toUserFromReq(newUserRequest)));
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!!!"));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUserList(List<Long> id, Pageable pageable) {
        if (id.isEmpty() || id == null) {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(id,pageable)
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
