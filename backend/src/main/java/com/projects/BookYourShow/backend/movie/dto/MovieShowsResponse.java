package com.projects.BookYourShow.backend.movie.dto;

import com.projects.BookYourShow.backend.show.dto.TheaterShowsResponse;

import java.time.LocalDate;
import java.util.List;

public record MovieShowsResponse(
        MovieResponse movie,
        LocalDate date,
        List<TheaterShowsResponse> theaters
) {}