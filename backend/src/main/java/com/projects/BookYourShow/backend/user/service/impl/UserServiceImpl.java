package com.projects.BookYourShow.backend.user.service.impl;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.mapper.UserMapper;
import com.projects.BookYourShow.backend.user.repository.UserRepository;
import com.projects.BookYourShow.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.email())) throw new ResourceNotFoundException("Email already exists");
        User user = userMapper.toEntity(userRequest);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User  user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        User  user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(userRequest.name());
        // user.setPassword(userRequest.password()); // TODO : logic for changing password via otp
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
}
