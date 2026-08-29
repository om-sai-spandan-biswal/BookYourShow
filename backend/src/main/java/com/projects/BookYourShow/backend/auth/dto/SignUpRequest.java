package com.projects.BookYourShow.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record SignUpRequest(
        @NotBlank(message = "Name should Not be blank")
        String name,
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Password should not be blank")
        String rawPassword
) {
}
