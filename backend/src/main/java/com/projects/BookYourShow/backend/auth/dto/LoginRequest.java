package com.projects.BookYourShow.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "please provide a valid email")
        String email,
        @NotBlank(message = "Password should not be blank")
        String rawPassword
) {
}
