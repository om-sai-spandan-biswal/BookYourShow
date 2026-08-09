package com.projects.BookYourShow.backend.theater.dto;

public record TheaterRequest(
        String name,
        String city,
        String address
) {}
