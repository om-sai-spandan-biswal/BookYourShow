package com.projects.BookYourShow.backend.booking.service.impl;

import com.projects.BookYourShow.backend.auth.service.AuthService;
import com.projects.BookYourShow.backend.booking.dto.BookingRequest;
import com.projects.BookYourShow.backend.booking.dto.BookingResponse;
import com.projects.BookYourShow.backend.booking.entity.Booking;
import com.projects.BookYourShow.backend.booking.entity.BookingSeat;
import com.projects.BookYourShow.backend.booking.enums.BookingStatus;
import com.projects.BookYourShow.backend.booking.mapper.BookingMapper;
import com.projects.BookYourShow.backend.booking.repository.BookingRepository;
import com.projects.BookYourShow.backend.booking.repository.BookingSeatRepository;
import com.projects.BookYourShow.backend.booking.service.BookingService;
import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ShowSeatService showSeatService;
    private final BookingSeatRepository bookingSeatRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public BookingResponse bookShow(BookingRequest bookingRequest) {
        List<ShowSeat> showSeats = showSeatService.getShowSeats(bookingRequest.showSeatIds());
        Long showId = showSeats.getFirst().getShow().getId();

        boolean sameShow = showSeats.stream()
                .allMatch(seat -> seat.getShow().getId().equals(showId));
        if (!sameShow) throw new ConflictException("Seats must belong to the same show");

        BigDecimal totalAmount = showSeats.stream()
                .map(ShowSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        User user = authService.getCurrentUser() ;

        Booking booking = Booking.builder()
                .user(user)
                .showId(showId)
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .build() ;

        booking = bookingRepository.save(booking);

        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) throw new ConflictException("Seat is not available");
            showSeat.setStatus(ShowSeatStatus.LOCKED);
            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .showSeat(showSeat)
                    .build();
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);
        booking.setBookingSeats(bookingSeats);

        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public void confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID " + bookingId));

        // Payment notifications may be delivered more than once.
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            bookingMapper.toResponse(booking);
            return;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ConflictException("Only pending bookings can be confirmed");
        }

        List<BookingSeat> bookingSeats = booking.getBookingSeats();
        if (bookingSeats.isEmpty() || bookingSeats.stream()
                .anyMatch(seat -> seat.getShowSeat().getStatus() != ShowSeatStatus.LOCKED)) {
            throw new ConflictException("Booking seats must be locked before confirmation");
        }

        bookingSeats.forEach(seat -> seat.getShowSeat().setStatus(ShowSeatStatus.BOOKED));
        booking.setStatus(BookingStatus.CONFIRMED);

        // The transaction persists changes to the managed booking and show seats together.
        bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID " + bookingId));

        User currentUser = authService.getCurrentUser();
        boolean admin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName() == UserRole.ADMIN);
        if (!admin && (booking.getUser() == null
                || !currentUser.getId().equals(booking.getUser().getId()))) {
            throw new AccessDeniedException("You can only cancel your own bookings");
        }

        // Do not release seats again: another booking may now hold them.
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ConflictException("Only pending bookings can be cancelled");
        }

        List<BookingSeat> bookingSeats = booking.getBookingSeats();
        if (bookingSeats.stream()
                .anyMatch(seat -> seat.getShowSeat().getStatus() != ShowSeatStatus.LOCKED)) {
            throw new ConflictException("Booking seats must be locked before cancellation");
        }

        bookingSeats.forEach(seat -> seat.getShowSeat().setStatus(ShowSeatStatus.AVAILABLE));
        booking.setStatus(BookingStatus.CANCELLED);
    }

}
