package com.projects.BookYourShow.backend.show.controller;

import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shows")
@RequiredArgsConstructor
public class ShowAdminController {
    private final ShowService showService;

    @GetMapping("/{showId}")
    public ResponseEntity<ShowResponse> getShow(
            @PathVariable Long showId)
    {
        return ResponseEntity.ok(showService.getShow(showId));
    }

    @PostMapping
    public ResponseEntity<ShowResponse> createShow(
            @RequestBody ShowRequest showRequest)
    {
        ShowResponse response = showService.createShow(showRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long showId)
    {
        showService.deleteShow(showId);
        return ResponseEntity.noContent().build();
    }
}
