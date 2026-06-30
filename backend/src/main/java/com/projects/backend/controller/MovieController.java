package com.projects.backend.controller;

import com.projects.backend.dto.MovieDetailsResponse;
import com.projects.backend.dto.MovieDto;
import com.projects.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMoviesOfCity(@RequestParam String city) {
        List<MovieDto> movies = movieService.getMoviesOfCity(city);
        return ResponseEntity.ok(movies);
    }

    @GetMapping(path = "/{movieId}")
    public ResponseEntity<MovieDetailsResponse> getMovie(@PathVariable Long movieId, @RequestParam String city) {
        MovieDetailsResponse movieDetails = movieService.getMovieDetails(city, movieId);
        return ResponseEntity.ok(movieDetails);
    }

}
