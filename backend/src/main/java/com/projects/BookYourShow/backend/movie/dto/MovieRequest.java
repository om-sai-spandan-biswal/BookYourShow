package com.projects.BookYourShow.backend.movie.dto;

import com.projects.BookYourShow.backend.movie.enums.Genre;
import com.projects.BookYourShow.backend.movie.enums.Language;
import com.projects.BookYourShow.backend.show.entity.Show;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovieRequest(
        @NotBlank
        String title,
        @NotBlank
        List<Genre> genres,
        @NotNull
        Double duration,
        @NotNull
        Language language
) {
}
