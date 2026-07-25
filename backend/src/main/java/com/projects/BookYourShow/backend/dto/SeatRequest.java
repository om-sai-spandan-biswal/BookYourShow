package com.projects.BookYourShow.backend.dto;

import com.projects.BookYourShow.backend.enums.SeatType;

public record SeatRequest(
         String rowName,
         Integer seatNumber,
         SeatType type
){}
