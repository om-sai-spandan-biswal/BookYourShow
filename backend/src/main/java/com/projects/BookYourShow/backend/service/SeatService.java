package com.projects.BookYourShow.backend.service;

import com.projects.BookYourShow.backend.dto.SeatRequest;
import com.projects.BookYourShow.backend.dto.SeatResponse;

import java.util.List;

public interface SeatService {
    List<SeatResponse> getSeatsOfScreen(Long screenId) ;
    SeatResponse createSeat(Long screenId, SeatRequest seatRequest);
    List<SeatResponse> createSeatsInBulk(Long screenId,List<SeatRequest> seatRequests);
    SeatResponse updateSeat(Long seatId, SeatRequest seatRequest);
    void deleteSeat(Long seatId);
}
