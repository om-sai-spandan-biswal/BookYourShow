package com.projects.backend.controller;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.service.MovieService;
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

    @GetMapping(path = "/{movieId}")
    public ResponseEntity<MovieDto>  getMovieById(@PathVariable Long movieId) {
        MovieDto movieDto = movieService.getMovieById(movieId);
        return ResponseEntity.ok(movieDto);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        MovieDto movie  = movieService.createMovie(movieDto);
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long movieId, @RequestBody MovieDto movieDto) {
        MovieDto movie  = movieService.updateMovie(movieId, movieDto);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }


}
