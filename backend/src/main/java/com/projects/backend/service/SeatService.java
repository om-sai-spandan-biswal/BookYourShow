package com.projects.backend.service;

import com.projects.backend.dto.SeatDto;

import java.util.List;

public interface SeatService {
    List<SeatDto> getAllSeats(Long screenId);
    SeatDto createSeat(Long screenId,SeatDto seatDto);
    SeatDto updateSeat(Long seatId, SeatDto seatDto);
    void deleteSeat(Long seatId);
}
