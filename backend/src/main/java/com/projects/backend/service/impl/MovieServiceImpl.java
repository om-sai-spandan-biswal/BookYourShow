package com.projects.backend.service.impl;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.dto.MovieDetailsResponse;
import com.projects.backend.dto.TheaterShowResponse;
import com.projects.backend.enitiy.Movie;
import com.projects.backend.enitiy.Show;
import com.projects.backend.enitiy.Theater;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.MovieMapper;
import com.projects.backend.mapper.ShowMapper;
import com.projects.backend.repository.MovieRepository;
import com.projects.backend.repository.ShowRepository;
import com.projects.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final MovieMapper movieMapper;
    private final ShowMapper showMapper;

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

    @Override
    public List<MovieDto> getMoviesOfCity(String city) {
        List<Movie> movies = movieRepository.findMoviesByCity(city);
        return movies.stream()
                .map(movieMapper::toDto)
                .toList() ;
    }

    @Override
    public MovieDetailsResponse getMovieDetails(String city, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId)) ;
        List<Show> shows = showRepository.findByMovieAndCity(movieId, city);
        Map<Theater, List<Show>> theaterShows = shows.stream()
                .collect(Collectors.groupingBy(show -> show.getScreen().getTheater()));

        List<TheaterShowResponse> theaterShowResponses = theaterShows.entrySet()
                .stream()
                .map(entry -> TheaterShowResponse.builder()
                        .theaterId(entry.getKey().getId())
                        .theaterName(entry.getKey().getName())
                        .address(entry.getKey().getAddress())
                        .shows(entry.getValue()
                                .stream()
                                .map(showMapper::toDto)
                                .toList())
                        .build())
                .toList();
        return MovieDetailsResponse.builder()
                .movie(movieMapper.toDto(movie))
                .theaters(theaterShowResponses)
                .build();
    }

}
