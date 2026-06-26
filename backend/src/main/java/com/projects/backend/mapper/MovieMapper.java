package com.projects.backend.mapper;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.enitiy.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieDto movieDto);
    MovieDto toDto(Movie movie);
}
