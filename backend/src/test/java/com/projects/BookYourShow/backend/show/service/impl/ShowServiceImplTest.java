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
import com.projects.BookYourShow.backend.movie.entity.Movie;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.repository.ShowRepository;
import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.theater.entity.Screen;
import com.projects.BookYourShow.backend.theater.entity.Theater;

import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.mapper.ShowMapper;
import com.projects.BookYourShow.backend.movie.mapper.MovieMapper;
import com.projects.BookYourShow.backend.movie.service.MovieService;
import com.projects.BookYourShow.backend.theater.service.ScreenService;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceImplTest {
    @Mock private ShowRepository repository;
    @Mock private ShowMapper mapper;
    @Mock private MovieService movies;
    @Mock private MovieMapper movieMapper;
    @Mock private ScreenService screens;
    @Mock private ShowSeatService seats;
    @InjectMocks private ShowServiceImpl service;

    private ShowRequest request() { return new ShowRequest(1L, 2L, LocalDateTime.of(2026, 11, 1, 10, 0), LocalDateTime.of(2026, 11, 1, 12, 0), new BigDecimal("200")); }
    @Test void getsAndDeletesShow() {
        Show show = new Show(); ShowResponse response = mock(ShowResponse.class);
        when(repository.findById(1L)).thenReturn(Optional.of(show)); when(mapper.toResponse(show)).thenReturn(response);
        assertSame(response, service.getShow(1L)); service.deleteShow(1L); verify(repository).delete(show);
    }
    @Test void missingShowCannotBeReadOrDeleted() {
        assertThrows(RuntimeException.class, () -> service.getShow(1L)); assertThrows(RuntimeException.class, () -> service.deleteShow(1L));
    }
    @Test void createsShowAndInitializesSeats() {
        Movie movie = new Movie(); Screen screen = Screen.builder().id(1L).build(); var request = request();
        when(movies.getMovie(2L)).thenReturn(movie); when(screens.getScreen(1L)).thenReturn(screen);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0)); service.createShow(request);
        verify(seats).createShowSeats(argThat(show -> show.getMovie() == movie && show.getScreen() == screen
            && show.getDate().equals(request.startTime().toLocalDate()) && show.getStartTime().equals(request.startTime())
            && show.getEndTime().equals(request.endTime()) && show.getBasePrice().equals(request.basePrice())));
    }
    @Test void overlappingShowIsRejectedBeforeSaving() {
        when(movies.getMovie(2L)).thenReturn(new Movie()); when(screens.getScreen(1L)).thenReturn(Screen.builder().id(1L).build());
        var request = request(); when(repository.existsOverlappingShow(1L, request.startTime().toLocalDate(), request.startTime(), request.endTime())).thenReturn(true);
        assertThrows(ConflictException.class, () -> service.createShow(request)); verify(repository, never()).save(any()); verifyNoInteractions(seats);
    }
    @Test void searchesByTitleAndCityAndRejectsMissingMovie() {
        Movie movie = new Movie(); MovieResponse response = mock(MovieResponse.class);
        when(repository.findMovieByTitleAndCity("Movie", "Pune")).thenReturn(Optional.of(movie)); when(movieMapper.toResponse(movie)).thenReturn(response);
        assertSame(response, service.searchMovieByTitleAndCity("Movie", "Pune"));
        assertThrows(ResourceNotFoundException.class, () -> service.searchMovieByTitleAndCity("Missing", "Pune"));
    }
    @Test void citySearchUsesPaginationAndMapsResults() {
        Movie movie = new Movie(); MovieResponse response = mock(MovieResponse.class);
        when(repository.findMoviesByCity(eq("Pune"), any())).thenReturn(new PageImpl<>(List.of(movie)));
        when(movieMapper.toResponse(movie)).thenReturn(response);
        assertEquals(List.of(response), service.searchMovieByCity("Pune", 2).getContent());
        verify(repository).findMoviesByCity("Pune", PageRequest.of(2, 5, Sort.by("movie.title").ascending()));
        assertThrows(IllegalArgumentException.class, () -> service.searchMovieByCity("Pune", -1));
    }
    @Test void groupsShowsByTheaterAndHandlesEmptyResults() {
        Movie movie = new Movie(); Theater theater = Theater.builder().id(1L).name("Cinema").address("Road").build();
        Screen screen = Screen.builder().theater(theater).build(); Show first = Show.builder().screen(screen).build();
        Show second = Show.builder().screen(screen).build(); ShowResponse response = mock(ShowResponse.class); LocalDate date = LocalDate.of(2026,11,1);
        when(movies.getMovie(2L)).thenReturn(movie); when(repository.findShowsByMovieAndCityAndDate(2L,"Pune",date)).thenReturn(List.of(first,second), List.of());
        when(mapper.toResponse(any())).thenReturn(response);
        var result = service.findShows(2L,"Pune",date);
        assertEquals(date, result.date()); assertEquals(1, result.theaters().size()); assertEquals(2, result.theaters().getFirst().shows().size());
        assertTrue(service.findShows(2L,"Pune",date).theaters().isEmpty());
    }

}
