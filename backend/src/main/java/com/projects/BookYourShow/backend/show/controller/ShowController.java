package com.projects.BookYourShow.backend.show.controller;

import com.projects.BookYourShow.backend.show.dto.ShowSeatResponse;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowSeatService showSeatService;

    @GetMapping(path = "/{showId}")
    public ResponseEntity<List<ShowSeatResponse>> getShowSeats(@PathVariable Long showId) {
        List<ShowSeatResponse> showSeats = showSeatService.getShowSeatsOfShow(showId);
        return ResponseEntity.ok(showSeats);
    }
}
