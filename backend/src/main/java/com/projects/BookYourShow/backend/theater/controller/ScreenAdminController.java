package com.projects.BookYourShow.backend.theater.controller;

import com.projects.BookYourShow.backend.theater.dto.ScreenRequest;
import com.projects.BookYourShow.backend.theater.dto.ScreenResponse;
import com.projects.BookYourShow.backend.theater.dto.SeatRequest;
import com.projects.BookYourShow.backend.theater.dto.SeatResponse;
import com.projects.BookYourShow.backend.theater.service.ScreenService;
import com.projects.BookYourShow.backend.theater.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/screens")
@RequiredArgsConstructor
public class ScreenAdminController {
    private final ScreenService screenService;
    private final SeatService seatService;


    @PutMapping("/{screenId}")
    public ResponseEntity<ScreenResponse> updateScreen(
            @PathVariable Long screenId,
            @Valid @RequestBody ScreenRequest request)
    {
        ScreenResponse response = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> deleteScreen(
            @PathVariable Long screenId)
    {
        screenService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }

    //Seats

    @GetMapping("/{screenId}/seats")
    public ResponseEntity<List<SeatResponse>> getAllSeatsOfScreen(
            @PathVariable Long screenId)
    {
        return ResponseEntity.ok(seatService.findSeatsOdScreen(screenId));
    }

    @PostMapping("/{screenId}/seats")
    public ResponseEntity<SeatResponse> createSeat(
            @PathVariable Long screenId,
            @Valid @RequestBody SeatRequest seatRequest) {

        SeatResponse response = seatService.createSeat(screenId, seatRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{screenId}/seats/bulk")
    public ResponseEntity<List<SeatResponse>> createSeatsInBulk(
            @PathVariable Long screenId,
            @Valid @RequestBody List<SeatRequest> seatRequests) {

        List<SeatResponse> response =
                seatService.createSeatsInBulk(screenId, seatRequests);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
