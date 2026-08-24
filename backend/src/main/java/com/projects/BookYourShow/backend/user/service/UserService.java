package com.projects.BookYourShow.backend.user.service;

import com.projects.BookYourShow.backend.user.entity.User;

public interface UserService {
    User getUserByEmail(String email);
    User saveUser(User user);
}
