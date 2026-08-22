package com.projects.BookYourShow.backend.movie.controller;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies/search")
@RequiredArgsConstructor
public class MovieBrowseController {
    private final ShowService showService;

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
}
