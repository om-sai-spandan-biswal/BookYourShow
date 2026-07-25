package com.projects.BookYourShow.backend.dto;

public record ScreenResponse (
        Long id,
        String name,
        Integer totalSeats,
        TheaterResponse theater,
        Boolean closed
) {}
