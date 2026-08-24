package com.projects.BookYourShow.backend.show.dto;

import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;

import java.math.BigDecimal;

public record ShowSeatResponse (
        Long id,
        Long showId,
        Long seatId,
        BigDecimal price,
        ShowSeatStatus status
){
}
