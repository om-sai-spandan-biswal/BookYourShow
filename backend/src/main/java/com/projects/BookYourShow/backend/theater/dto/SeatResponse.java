package com.projects.BookYourShow.backend.theater.dto;

import com.projects.BookYourShow.backend.theater.entity.Seat;
import com.projects.BookYourShow.backend.theater.enums.SeatType;

public record SeatResponse(
        Long id,
        String rowName,
        Integer seatNumber,
        SeatType type
){}
