package com.projects.BookYourShow.backend.booking.dto;

import com.projects.BookYourShow.backend.booking.enums.BookingStatus;

import java.math.BigDecimal;
import java.util.List;

public record BookingResponse(
        Long id,
        Long userId,
        Long showId,
        BookingStatus status,
        BigDecimal totalAmount,
        List<Long> showSeatIds
) {
}
