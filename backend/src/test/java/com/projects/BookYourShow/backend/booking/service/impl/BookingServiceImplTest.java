package com.projects.BookYourShow.backend.booking.service.impl;

import com.projects.BookYourShow.backend.auth.service.AuthService;
import com.projects.BookYourShow.backend.booking.entity.Booking;
import com.projects.BookYourShow.backend.booking.entity.BookingSeat;
import com.projects.BookYourShow.backend.booking.enums.BookingStatus;
import com.projects.BookYourShow.backend.booking.mapper.BookingMapper;
import com.projects.BookYourShow.backend.booking.repository.BookingRepository;
import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private AuthService authService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void confirmsPendingBookingAndAllLockedSeats() {
        ShowSeat first = seat(ShowSeatStatus.LOCKED);
        ShowSeat second = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, first, second);

        bookingService.confirmBooking(1L);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(ShowSeatStatus.BOOKED, first.getStatus());
        assertEquals(ShowSeatStatus.BOOKED, second.getStatus());
        verify(bookingMapper).toResponse(booking);
    }

    @Test
    void repeatedConfirmationReturnsExistingBooking() {
        Booking booking = booking(BookingStatus.CONFIRMED, seat(ShowSeatStatus.BOOKED));

        bookingService.confirmBooking(1L);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        verify(bookingMapper).toResponse(booking);
    }

    @Test
    void rejectsMissingBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.confirmBooking(1L));
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void rejectsCancelledBooking() {
        ShowSeat seat = seat(ShowSeatStatus.AVAILABLE);
        Booking booking = booking(BookingStatus.CANCELLED, seat);

        assertThrows(ConflictException.class, () -> bookingService.confirmBooking(1L));

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, seat.getStatus());
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void rejectsUnlockedSeatsBeforeChangingAnySeat() {
        ShowSeat locked = seat(ShowSeatStatus.LOCKED);
        ShowSeat available = seat(ShowSeatStatus.AVAILABLE);
        Booking booking = booking(BookingStatus.PENDING, locked, available);

        assertThrows(ConflictException.class, () -> bookingService.confirmBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, locked.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, available.getStatus());
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void rejectsBookingWithoutSeats() {
        Booking booking = booking(BookingStatus.PENDING);

        assertThrows(ConflictException.class, () -> bookingService.confirmBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void cancelsPendingBookingAndReleasesAllSeats() {
        ShowSeat first = seat(ShowSeatStatus.LOCKED);
        ShowSeat second = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, first, second);
        authenticateOwner(booking);

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, first.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, second.getStatus());
    }

    @Test
    void repeatedCancellationDoesNotReleaseSeatsHeldByAnotherBooking() {
        ShowSeat seat = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.CANCELLED, seat);
        authenticateOwner(booking);

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, seat.getStatus());
    }

    @Test
    void cannotCancelConfirmedBooking() {
        ShowSeat seat = seat(ShowSeatStatus.BOOKED);
        Booking booking = booking(BookingStatus.CONFIRMED, seat);
        authenticateOwner(booking);

        assertThrows(ConflictException.class, () -> bookingService.cancelBooking(1L));

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(ShowSeatStatus.BOOKED, seat.getStatus());
    }

    @Test
    void cancellationRejectsMissingBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.cancelBooking(1L));
    }

    @Test
    void cancellationValidatesAllSeatsBeforeReleasingAny() {
        ShowSeat locked = seat(ShowSeatStatus.LOCKED);
        ShowSeat booked = seat(ShowSeatStatus.BOOKED);
        Booking booking = booking(BookingStatus.PENDING, locked, booked);
        authenticateOwner(booking);

        assertThrows(ConflictException.class, () -> bookingService.cancelBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, locked.getStatus());
        assertEquals(ShowSeatStatus.BOOKED, booked.getStatus());
    }

    @Test
    void cannotCancelAnotherUsersBooking() {
        ShowSeat seat = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, seat);
        authenticateOwner(booking);
        User otherUser = new User();
        otherUser.setId(2L);
        when(authService.getCurrentUser()).thenReturn(otherUser);

        assertThrows(AccessDeniedException.class, () -> bookingService.cancelBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, seat.getStatus());
    }

    @Test
    void adminCanCancelAnotherUsersPendingBooking() {
        ShowSeat seat = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, seat);
        authenticateOwner(booking);
        User admin = new User();
        admin.setId(2L);
        admin.setRoles(Set.of(new Role(UserRole.ADMIN)));
        when(authService.getCurrentUser()).thenReturn(admin);

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, seat.getStatus());
    }

    private void authenticateOwner(Booking booking) {
        User user = new User();
        user.setId(1L);
        booking.setUser(user);
        when(authService.getCurrentUser()).thenReturn(user);
    }

    private Booking booking(BookingStatus status, ShowSeat... seats) {
        Booking booking = Booking.builder()
                .id(1L)
                .status(status)
                .bookingSeats(List.of(seats).stream()
                        .map(seat -> BookingSeat.builder().showSeat(seat).build())
                        .toList())
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        return booking;
    }

    private ShowSeat seat(ShowSeatStatus status) {
        return ShowSeat.builder().status(status).build();
    }
}
