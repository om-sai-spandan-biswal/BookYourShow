package com.projects.BookYourShow.backend.booking.service;

import com.projects.BookYourShow.backend.booking.dto.BookingRequest;
import com.projects.BookYourShow.backend.booking.dto.BookingResponse;

public interface BookingService {
    BookingResponse bookShow(BookingRequest bookingRequest);
    BookingResponse confirmBooking(Long bookingId);
    void cancelBooking(Long bookingId);

}
