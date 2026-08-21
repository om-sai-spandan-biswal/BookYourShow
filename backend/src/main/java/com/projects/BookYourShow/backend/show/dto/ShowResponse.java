package com.projects.BookYourShow.backend.show.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ShowResponse(
        Long id,
        Long screenId,
        Long movieId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice
) {
}
