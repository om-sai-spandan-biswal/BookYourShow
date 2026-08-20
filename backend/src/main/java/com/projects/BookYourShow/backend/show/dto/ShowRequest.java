package com.projects.BookYourShow.backend.show.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ShowRequest (
        Long screenId,
        Long movieId,
        LocalDate date,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice
){
}
