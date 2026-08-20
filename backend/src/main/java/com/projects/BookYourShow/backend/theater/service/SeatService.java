package com.projects.BookYourShow.backend.theater.service;

import com.projects.BookYourShow.backend.theater.dto.SeatRequest;
import com.projects.BookYourShow.backend.theater.dto.SeatResponse;
import com.projects.BookYourShow.backend.theater.entity.Seat;

import java.util.List;

public interface SeatService {
    List<SeatResponse> findSeatsOdScreen(Long screenId);
    List<Seat> getSeatsOfScreen(Long screenId) ;
    SeatResponse createSeat(Long screenId, SeatRequest seatRequest);
    List<SeatResponse> createSeatsInBulk(Long screenId,List<SeatRequest> seatRequests);
    SeatResponse updateSeat(Long seatId, SeatRequest seatRequest);
    void deleteSeat(Long seatId);
}
