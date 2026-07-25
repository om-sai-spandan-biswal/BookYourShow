package com.projects.BookYourShow.backend.dto;

import com.projects.BookYourShow.backend.enums.SeatType;

public record SeatResponse(
        Long id,
        String rowName,
        Integer seatNumber,
        SeatType type,
        String screenId
){}
