package com.projects.BookYourShow.backend.booking.dto;

import java.util.List;

public record BookingRequest(
        List<Long> showSeatIds
) {
}
