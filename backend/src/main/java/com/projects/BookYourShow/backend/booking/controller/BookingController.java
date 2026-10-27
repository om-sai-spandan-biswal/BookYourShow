package com.projects.BookYourShow.backend.booking.controller;

import com.projects.BookYourShow.backend.booking.dto.BookingRequest;
import com.projects.BookYourShow.backend.booking.dto.BookingResponse;
import com.projects.BookYourShow.backend.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingResponse> bookShow(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.bookShow(bookingRequest));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

}
