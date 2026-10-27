package com.projects.BookYourShow.backend.theater.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import java.math.BigDecimal;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projects.BookYourShow.backend.theater.entity.Screen;
import com.projects.BookYourShow.backend.theater.entity.Seat;
import com.projects.BookYourShow.backend.theater.repository.ScreenRepository;
import com.projects.BookYourShow.backend.theater.repository.SeatRepository;
import com.projects.BookYourShow.backend.theater.dto.SeatRequest;
import com.projects.BookYourShow.backend.theater.dto.SeatResponse;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.theater.mapper.SeatMapper;
import com.projects.BookYourShow.backend.theater.enums.SeatType;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {
    @Mock private SeatRepository repository;
    @Mock private ScreenRepository screens;
    @Mock private SeatMapper mapper;
    @InjectMocks private SeatServiceImpl service;

    private SeatRequest request() { return new SeatRequest("B", 3, SeatType.values()[0]); }
    @Test void listsEntitiesAndResponses() {
        Screen screen = new Screen(); Seat seat = new Seat(); SeatResponse response = mock(SeatResponse.class);
        when(screens.findById(1L)).thenReturn(Optional.of(screen)); when(repository.findByScreen(screen)).thenReturn(List.of(seat));
        when(mapper.toResponse(seat)).thenReturn(response);
        assertEquals(List.of(seat), service.getSeatsOfScreen(1L)); assertEquals(List.of(response), service.findSeatsOdScreen(1L));
    }
    @Test void createsSeatAttachedToScreen() {
        Screen screen = new Screen(); Seat seat = new Seat(); var request = request();
        when(screens.findById(1L)).thenReturn(Optional.of(screen)); when(mapper.toEntity(request)).thenReturn(seat);
        when(repository.save(seat)).thenReturn(seat); service.createSeat(1L, request); assertSame(screen, seat.getScreen()); verify(mapper).toResponse(seat);
    }
    @Test void bulkCreationAttachesEverySeat() {
        Screen screen = new Screen(); Seat first = new Seat(), second = new Seat(); var request = request();
        SeatResponse response = mock(SeatResponse.class);
        when(screens.findById(1L)).thenReturn(Optional.of(screen)); when(mapper.toEntity(request)).thenReturn(first, second);
        when(mapper.toResponse(any())).thenReturn(response);
        assertEquals(List.of(response, response), service.createSeatsInBulk(1L, List.of(request, request)));
        assertSame(screen, first.getScreen()); assertSame(screen, second.getScreen()); verify(repository).saveAll(List.of(first, second));
    }
    @Test void updatesAllSeatFieldsAndDeletes() {
        Seat seat = new Seat(); when(repository.findById(1L)).thenReturn(Optional.of(seat)); when(repository.save(seat)).thenReturn(seat);
        service.updateSeat(1L, request()); assertEquals("B", seat.getRowName()); assertEquals(3, seat.getSeatNumber());
        assertEquals(request().type(), seat.getType()); service.deleteSeat(1L); verify(repository).delete(seat);
    }
    @Test void rejectsMissingScreensAndSeats() {
        assertThrows(ResourceNotFoundException.class, () -> service.findSeatsOdScreen(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.getSeatsOfScreen(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.createSeat(1L, request()));
        assertThrows(ResourceNotFoundException.class, () -> service.createSeatsInBulk(1L, List.of(request())));
        assertThrows(ResourceNotFoundException.class, () -> service.updateSeat(1L, request()));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteSeat(1L)); verify(repository, never()).save(any());
    }

}
