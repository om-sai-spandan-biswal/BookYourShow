package com.projects.BookYourShow.backend.show.dto;

import java.util.List;

public record TheaterShowsResponse(
        Long theaterId,
        String theaterName,
        String address,
        List<ShowResponse> shows
) {}