package com.projects.BookYourShow.backend.movie.controller;

import com.projects.BookYourShow.backend.movie.dto.MovieRequest;
import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieAdminController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findMovies()
    {
        return ResponseEntity.ok(movieService.findAllMovie());
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> findMovie(
            @PathVariable Long movieId)
    {
        return ResponseEntity.ok(movieService.findMovie(movieId));
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(
            @RequestBody MovieRequest movieRequest)
    {
        MovieResponse response = movieService.createMovie(movieRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable Long movieId,
            @RequestBody MovieRequest movieRequest)
    {
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieRequest));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long movieId)
    {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}
