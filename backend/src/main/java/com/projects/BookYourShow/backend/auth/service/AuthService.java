package com.projects.BookYourShow.backend.auth.service;

import com.projects.BookYourShow.backend.auth.dto.AuthTokens;
import com.projects.BookYourShow.backend.auth.dto.LoginRequest;
import com.projects.BookYourShow.backend.auth.dto.SignUpRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import com.projects.BookYourShow.backend.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserResponse signUp(SignUpRequest signUpRequest);
    AuthTokens login(LoginRequest loginRequest);
    AuthTokens refreshToken(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    User getCurrentUser();
}
