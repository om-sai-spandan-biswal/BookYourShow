package com.projects.BookYourShow.backend.user.dto;

import java.util.Set;

public record UserResponse(
        Long id,
        String name,
        String email,
        String password,
        Set<String> roles,
        Boolean enabled
) {
}
