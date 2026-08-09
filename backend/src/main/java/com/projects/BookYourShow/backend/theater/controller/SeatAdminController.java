package com.projects.BookYourShow.backend.theater.controller;

import com.projects.BookYourShow.backend.theater.dto.SeatRequest;
import com.projects.BookYourShow.backend.theater.dto.SeatResponse;
import com.projects.BookYourShow.backend.theater.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/screens")
@RequiredArgsConstructor
public class SeatAdminController {
    private final SeatService seatService;

    @PutMapping("/{seatId}")
    public ResponseEntity<SeatResponse> updateSeat(
            @PathVariable Long seatId,
            @Valid @RequestBody SeatRequest seatRequest) {

        SeatResponse response = seatService.updateSeat(seatId, seatRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> deleteSeat(
            @PathVariable Long seatId) {

        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }
}
