package com.projects.BookYourShow.backend.dto;

public record TheaterResponse(
        Long id,
        String name,
        String city,
        String address,
        Boolean active
) {
}
