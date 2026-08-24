package com.projects.BookYourShow.backend.user.service.impl;

import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.repository.UserRepository;
import com.projects.BookYourShow.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
