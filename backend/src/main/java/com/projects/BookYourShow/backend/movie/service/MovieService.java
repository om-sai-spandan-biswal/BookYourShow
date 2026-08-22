package com.projects.BookYourShow.backend.movie.service;

import com.projects.BookYourShow.backend.movie.dto.MovieRequest;
import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.entity.Movie;

import java.util.List;

public interface MovieService {
    Movie getMovie(Long movieId);
    MovieResponse findMovie(Long movieId);
    List<MovieResponse> findAllMovie();
    MovieResponse createMovie(MovieRequest movieRequest);
    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);
    void deleteMovie(Long movieId);
}
