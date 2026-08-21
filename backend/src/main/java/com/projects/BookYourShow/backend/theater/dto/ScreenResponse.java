package com.projects.BookYourShow.backend.theater.dto;

public record ScreenResponse (
        Long id,
        String name,
        TheaterResponse theater,
        boolean active
) {}
