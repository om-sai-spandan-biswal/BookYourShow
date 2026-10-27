package com.projects.BookYourShow.backend.show.service.impl;

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

import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.repository.ShowRepository;
import com.projects.BookYourShow.backend.show.repository.ShowSeatRepository;
import com.projects.BookYourShow.backend.show.dto.ShowSeatResponse;
import com.projects.BookYourShow.backend.theater.entity.Screen;
import com.projects.BookYourShow.backend.theater.entity.Seat;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.mapper.ShowSeatMapper;
import com.projects.BookYourShow.backend.theater.service.SeatService;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;

@ExtendWith(MockitoExtension.class)
class ShowSeatServiceImplTest {
    @Mock private ShowRepository shows;
    @Mock private ShowSeatRepository repository;
    @Mock private SeatService seats;
    @Mock private ShowSeatMapper mapper;
    @InjectMocks private ShowSeatServiceImpl service;

    @Test void createsAvailableSeatsWithShowPrice() {
        Screen screen = Screen.builder().id(1L).build(); Show show = Show.builder().screen(screen).basePrice(new BigDecimal("250")).build();
        Seat first = new Seat(), second = new Seat(); when(seats.getSeatsOfScreen(1L)).thenReturn(List.of(first, second));
        service.createShowSeats(show);
        verify(repository).saveAll(argThat(list -> {
            List<ShowSeat> values = new ArrayList<>(); list.forEach(values::add);
            return values.size() == 2 && values.get(0).getSeat() == first && values.get(1).getSeat() == second
                && values.stream().allMatch(s -> s.getShow() == show && s.getPrice().equals(show.getBasePrice()) && s.getStatus() == ShowSeatStatus.AVAILABLE);
        }));
    }
    @Test void getsRequestedSeatEntities() {
        ShowSeat seat = new ShowSeat(); when(repository.findAllById(List.of(1L))).thenReturn(List.of(seat));
        assertEquals(List.of(seat), service.getShowSeats(List.of(1L)));
    }
    @Test void listsMappedSeatsForShow() {
        Show show = new Show(); ShowSeat seat = new ShowSeat(); ShowSeatResponse response = mock(ShowSeatResponse.class);
        when(shows.findById(1L)).thenReturn(Optional.of(show)); when(repository.findByShow(show)).thenReturn(List.of(seat));
        when(mapper.toResponse(seat)).thenReturn(response); assertEquals(List.of(response), service.getShowSeatsOfShow(1L));
    }
    @Test void rejectsMissingShow() {
        assertThrows(ResourceNotFoundException.class, () -> service.getShowSeatsOfShow(1L)); verifyNoInteractions(repository);
    }

}
