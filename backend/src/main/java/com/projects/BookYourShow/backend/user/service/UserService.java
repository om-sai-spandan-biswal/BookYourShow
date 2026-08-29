package com.projects.BookYourShow.backend.user.service;

import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import com.projects.BookYourShow.backend.user.entity.User;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    User getUserByEmail(String email);
    UserResponse  getUserById(Long userId);
    UserResponse  updateUserById(Long userId, UserRequest userRequest);
}
