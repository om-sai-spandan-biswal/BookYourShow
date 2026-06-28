package com.projects.backend.controller;

import com.projects.backend.dto.ShowDto;
import com.projects.backend.service.MovieService;
import com.projects.backend.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/shows")
@RequiredArgsConstructor
public class ShowAdminController {

    private final ShowService showService;

    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows() {
        List<ShowDto> shows = showService.getShows();
        return ResponseEntity.ok(shows);
    }

    @GetMapping(path = "/{showId}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long showId) {
        ShowDto show = showService.getShowById(showId);
        return ResponseEntity.ok(show);
    }

    @PostMapping(path = "/{screenId}/{movieId}")
    public ResponseEntity<ShowDto> createShow(
            @PathVariable Long movieId,
            @PathVariable Long screenId,
            @RequestBody ShowDto showDto) {
        ShowDto show = showService.createShow(movieId, screenId, showDto);
        return new ResponseEntity<>(show, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{screenId}/{movieId}/{showId}")
    public ResponseEntity<ShowDto> updateShow(
            @PathVariable Long screenId,
            @PathVariable Long movieId,
            @PathVariable Long showId,
            @RequestBody ShowDto showDto
    ) {
        ShowDto show = showService.updateShow(screenId, movieId, showId, showDto);
        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{showId}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return ResponseEntity.noContent().build();
    }
}
