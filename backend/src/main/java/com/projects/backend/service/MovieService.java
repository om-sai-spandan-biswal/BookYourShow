package com.projects.backend.service;

import com.projects.backend.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto getMovieById(Long movieId);
    List<MovieDto> getAllMovies();
    MovieDto createMovie(MovieDto movieDto);
    MovieDto updateMovie(Long movieId, MovieDto movieDto);
    void deleteMovie(Long movieId);
}
