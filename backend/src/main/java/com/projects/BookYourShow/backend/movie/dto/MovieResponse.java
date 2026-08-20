package com.projects.BookYourShow.backend.movie.dto;

import com.projects.BookYourShow.backend.movie.enums.Genre;
import com.projects.BookYourShow.backend.movie.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovieResponse(
        Long id,
        String title,
        List<Genre> genres,
        Double duration,
        Language language,
        boolean deleted
) {
}
