package com.projects.BookYourShow.backend.movie.service.impl;

import com.projects.BookYourShow.backend.movie.dto.MovieRequest;
import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.dto.MovieShowsResponse;
import com.projects.BookYourShow.backend.movie.entity.Movie;
import com.projects.BookYourShow.backend.movie.mapper.MovieMapper;
import com.projects.BookYourShow.backend.movie.repository.MovieRepository;
import com.projects.BookYourShow.backend.movie.service.MovieService;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.dto.TheaterShowsResponse;
import com.projects.BookYourShow.backend.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    // private final ShowService showService; NOT DO THIS , make a dependency loop

    @Override
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Exist with ID "+movieId));
    }

    @Override
    public MovieResponse findMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Exist with ID "+movieId));
        if(movie.isDeleted()) throw new RuntimeException("Movie Already Deleted");
        return movieMapper.toResponse(movie);
    }

    @Override
    public List<MovieResponse> findAllMovie() {
        return movieRepository.findAll().stream().map(movieMapper::toResponse).toList();
    }

    @Override
    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie movie = movieMapper.toEntity(movieRequest);
        movie.setDeleted(false);
        movie = movieRepository.save(movie);
        return movieMapper.toResponse(movie);
    }

    @Override
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Exist with ID "+movieId));

        if(movie.isDeleted()) throw new RuntimeException("Movie Already Deleted");

        movie.setTitle(movieRequest.title());
        movie.setDuration(movieRequest.duration());
        movie.setGenres(movieRequest.genres());
        movie.setLanguage(movieRequest.language());

        movie = movieRepository.save(movie);
        return movieMapper.toResponse(movie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Exist with ID "+movieId));
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

}
