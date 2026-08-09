package com.projects.BookYourShow.backend.theater.dto;

public record TheaterResponse(
        Long id,
        String name,
        String city,
        String address,
        Boolean active
) {
}
