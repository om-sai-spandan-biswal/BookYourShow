package com.projects.backend.service.impl;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.enitiy.Movie;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.MovieMapper;
import com.projects.backend.repository.MovieRepository;
import com.projects.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId)) ;
        return movieMapper.toDto(movie);
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::toDto)
                .toList() ;
    }

    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = movieMapper.toEntity(movieDto);
        movieRepository.save(movie);
        return movieMapper.toDto(movie);
    }

    @Override
    public MovieDto updateMovie(Long movieId, MovieDto movieDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId)) ;
        movie.setGenres(movieDto.getGenres());
        movie.setDuration(movieDto.getDuration());
        movie.setLanguage(movieDto.getLanguage());
        movie.setTitle(movieDto.getTitle());

        movieRepository.save(movie);
        return movieMapper.toDto(movie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId)) ;
        movieRepository.delete(movie);
    }

}
