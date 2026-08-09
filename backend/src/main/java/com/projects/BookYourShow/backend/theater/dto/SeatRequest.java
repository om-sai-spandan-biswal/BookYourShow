package com.projects.BookYourShow.backend.theater.dto;

import com.projects.BookYourShow.backend.theater.enums.SeatType;

public record SeatRequest(
         String rowName,
         Integer seatNumber,
         SeatType type
){}
