package com.projects.backend.controller;

import com.projects.backend.dto.SeatDto;
import com.projects.backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/seats")
@RequiredArgsConstructor
public class SeatAdminController {
    private final SeatService seatService;

    @GetMapping(path = "/{screenId}")
    public ResponseEntity<List<SeatDto>> getAllScreenSeats(@PathVariable Long screenId) {
        List<SeatDto> seats = seatService.getAllSeats(screenId);
        return ResponseEntity.ok(seats);
    }

    @PostMapping(path = "/{screenId}")
    public ResponseEntity<SeatDto> createSeat(@PathVariable Long screenId, @RequestBody SeatDto seatDto) {
        SeatDto seat = seatService.createSeat(screenId, seatDto);
        return new ResponseEntity<>(seat, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{seatId}")
    public ResponseEntity<SeatDto> updateSeat(@PathVariable Long seatId, @RequestBody SeatDto seatDto) {
        SeatDto seat = seatService.updateSeat(seatId, seatDto);
        return new ResponseEntity<>(seat, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }

}
