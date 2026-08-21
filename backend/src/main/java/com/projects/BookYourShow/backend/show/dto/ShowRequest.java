package com.projects.BookYourShow.backend.show.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowRequest (
        Long screenId,
        Long movieId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice
){
}
