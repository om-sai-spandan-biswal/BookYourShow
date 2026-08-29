package com.projects.BookYourShow.backend.movie.controller;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.dto.MovieShowsResponse;
import com.projects.BookYourShow.backend.movie.service.MovieService;
import com.projects.BookYourShow.backend.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/movies/search")
@RequiredArgsConstructor
public class MovieBrowseController {
    private final ShowService showService;
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<MovieResponse> getMoviesByTitleAndCity(
            @RequestParam(name = "city") String city,
            @RequestParam(name = "title", required = false) String title
    ) {
        MovieResponse movie = showService.searchMovieByTitleAndCity(title, city) ;
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<MovieResponse>> getMovieByCity(
            @RequestParam(name = "city") String city,
            @RequestParam(defaultValue = "0",required = false) Integer number
    ) {
        Page<MovieResponse> movies = showService.searchMovieByCity(city,number) ;
        return ResponseEntity.ok(movies);
    }

    @GetMapping(path = "/{movieId}")
    public ResponseEntity<MovieResponse> getMovieById(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(movieService.findMovie(movieId)) ;
    }

    @GetMapping(path = "/{movieId}/shows")
    public ResponseEntity<MovieShowsResponse> getShowsByMovieId(
            @PathVariable Long movieId,
            @RequestParam(name = "city") String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
         MovieShowsResponse response = showService.findShows(movieId, city, date);
        return ResponseEntity.ok(response);
    }



}
