package com.projects.BookYourShow.backend.booking.service.impl;

import com.projects.BookYourShow.backend.auth.service.AuthService;
import com.projects.BookYourShow.backend.booking.entity.Booking;
import com.projects.BookYourShow.backend.booking.dto.BookingRequest;
import com.projects.BookYourShow.backend.booking.repository.BookingSeatRepository;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.booking.entity.BookingSeat;
import com.projects.BookYourShow.backend.booking.enums.BookingStatus;
import com.projects.BookYourShow.backend.booking.mapper.BookingMapper;
import com.projects.BookYourShow.backend.booking.repository.BookingRepository;
import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import com.projects.BookYourShow.backend.show.repository.ShowSeatRepository;
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
import java.math.BigDecimal;

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
    @Mock
    private ShowSeatRepository showSeatRepository;
    @Mock
    private ShowSeatService showSeatService;
    @Mock
    private BookingSeatRepository bookingSeatRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createsPendingBookingWithTotalAndLocksSeats() {
        Show show = Show.builder().id(10L).build();
        ShowSeat first = seat(ShowSeatStatus.AVAILABLE), second = seat(ShowSeatStatus.AVAILABLE);
        first.setShow(show); second.setShow(show);
        first.setPrice(new BigDecimal("200.00")); second.setPrice(new BigDecimal("250.00"));
        List<Long> ids = List.of(first.getId(), second.getId());
        User user = new User();
        when(showSeatService.getShowSeats(ids)).thenReturn(List.of(first, second));
        when(authService.getCurrentUser()).thenReturn(user);
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        bookingService.bookShow(new BookingRequest(ids));

        assertEquals(ShowSeatStatus.LOCKED, first.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, second.getStatus());
        verify(bookingMapper).toResponse(argThat(booking -> booking.getStatus() == BookingStatus.PENDING
                && booking.getUser() == user && booking.getShowId().equals(10L)
                && booking.getTotalAmount().equals(new BigDecimal("450.00"))
                && booking.getBookingSeats().size() == 2
                && booking.getBookingSeats().stream().allMatch(seat -> seat.getBooking() == booking)));
        verify(bookingSeatRepository).saveAll(argThat(seats -> {
            List<BookingSeat> values = new java.util.ArrayList<>(); seats.forEach(values::add);
            return values.size() == 2 && values.get(0).getShowSeat() == first && values.get(1).getShowSeat() == second;
        }));
    }

    @Test
    void rejectsSeatsFromDifferentShowsBeforeCreatingBooking() {
        ShowSeat first = seat(ShowSeatStatus.AVAILABLE), second = seat(ShowSeatStatus.AVAILABLE);
        first.setShow(Show.builder().id(1L).build()); second.setShow(Show.builder().id(2L).build());
        List<Long> ids = List.of(first.getId(), second.getId());
        when(showSeatService.getShowSeats(ids)).thenReturn(List.of(first, second));

        assertThrows(ConflictException.class, () -> bookingService.bookShow(new BookingRequest(ids)));

        verifyNoInteractions(bookingRepository, bookingSeatRepository, authService);
        assertEquals(ShowSeatStatus.AVAILABLE, first.getStatus());
    }

    @Test
    void rejectsUnavailableSeatDuringBookingCreation() {
        ShowSeat seat = seat(ShowSeatStatus.BOOKED);
        seat.setShow(Show.builder().id(1L).build()); seat.setPrice(BigDecimal.TEN);
        when(showSeatService.getShowSeats(List.of(seat.getId()))).thenReturn(List.of(seat));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThrows(ConflictException.class, () -> bookingService.bookShow(new BookingRequest(List.of(seat.getId()))));

        assertEquals(ShowSeatStatus.BOOKED, seat.getStatus());
        verifyNoInteractions(bookingSeatRepository, bookingMapper);
    }

    @Test
    void confirmsPendingBookingAndAllLockedSeats() {
        ShowSeat first = seat(ShowSeatStatus.LOCKED);
        ShowSeat second = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, first, second);
        when(showSeatRepository.findAndLockAvailableShowSeats(List.of(first.getId(), second.getId())))
                .thenReturn(List.of(first, second));

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
        when(showSeatRepository.findAndLockAvailableShowSeats(List.of(locked.getId(), available.getId())))
                .thenReturn(List.of(locked));

        assertThrows(ConflictException.class, () -> bookingService.confirmBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, locked.getStatus());
        assertEquals(ShowSeatStatus.AVAILABLE, available.getStatus());
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void rejectsSeatsAlreadyClaimedByAnotherConfirmation() {
        ShowSeat seat = seat(ShowSeatStatus.LOCKED);
        Booking booking = booking(BookingStatus.PENDING, seat);
        when(showSeatRepository.findAndLockAvailableShowSeats(List.of(seat.getId())))
                .thenReturn(List.of());

        assertThrows(ConflictException.class, () -> bookingService.confirmBooking(1L));

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(ShowSeatStatus.LOCKED, seat.getStatus());
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
        return ShowSeat.builder().id(nextSeatId++).status(status).build();
    }

    private long nextSeatId = 1L;
}
