package com.projects.BookYourShow.backend.movie.mapper;

import com.projects.BookYourShow.backend.movie.dto.MovieRequest;
import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieRequest movieRequest);
    MovieResponse toResponse(Movie movie);
}
