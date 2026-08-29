package com.projects.BookYourShow.backend.auth.dto;

public record TokenResponse(
        String accessToken,
        long expireIn
) {
}
