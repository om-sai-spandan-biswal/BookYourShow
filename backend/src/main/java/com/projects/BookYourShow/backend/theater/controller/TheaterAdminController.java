package com.projects.BookYourShow.backend.theater.controller;

import com.projects.BookYourShow.backend.theater.dto.ScreenRequest;
import com.projects.BookYourShow.backend.theater.dto.ScreenResponse;
import com.projects.BookYourShow.backend.theater.dto.TheaterRequest;
import com.projects.BookYourShow.backend.theater.dto.TheaterResponse;
import com.projects.BookYourShow.backend.theater.service.ScreenService;
import com.projects.BookYourShow.backend.theater.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/theaters")
@RequiredArgsConstructor
public class TheaterAdminController {
    private final TheaterService theaterService;
    private final ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> getTheaters() {
        return ResponseEntity.ok(theaterService.getTheaters());
    }

    @PostMapping
    public ResponseEntity<TheaterResponse> createTheater(
            @Valid @RequestBody TheaterRequest theaterRequest)
    {
        TheaterResponse response = theaterService.createTheater(theaterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{theaterId}")
    public ResponseEntity<TheaterResponse> updateTheater(
            @PathVariable Long theaterId,
            @Valid @RequestBody TheaterRequest theaterRequest)
    {
        TheaterResponse response = theaterService.updateTheater(theaterId, theaterRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{theaterId}/activate")
    public ResponseEntity<Void> activateTheater(
            @PathVariable Long theaterId)
    {
        theaterService.activateTheater(theaterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{theaterId}")
    public ResponseEntity<Void> deleteTheater(
            @PathVariable Long theaterId)
    {
        theaterService.deleteTheater(theaterId);
        return ResponseEntity.noContent().build();
    }

    // Screens

    @GetMapping("/{theaterId}/screens")
    public ResponseEntity<List<ScreenResponse>> getAllScreensOfTheater(
            @PathVariable Long theaterId)
    {
        return ResponseEntity.ok(screenService.getAllScreensOfTheater(theaterId));
    }

    @PostMapping("/{theaterId}/screens")
    public ResponseEntity<ScreenResponse> createScreen(
            @PathVariable Long theaterId,
            @Valid @RequestBody ScreenRequest request)
    {
        ScreenResponse response = screenService.createScreen(theaterId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
