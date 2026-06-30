package com.projects.backend.service;

import com.projects.backend.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);

}
