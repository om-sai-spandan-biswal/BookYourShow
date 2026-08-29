package com.projects.BookYourShow.backend.user.dto;

import com.projects.BookYourShow.backend.user.entity.Role;

import java.util.Set;

public record UserRequest(
        String name,
        String email,
        String password,
        Set<Role> roles,
        Boolean enabled
) {
}
