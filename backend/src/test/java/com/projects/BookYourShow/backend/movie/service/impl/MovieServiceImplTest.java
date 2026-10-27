package com.projects.BookYourShow.backend.movie.service.impl;

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
import com.projects.BookYourShow.backend.movie.repository.MovieRepository;
import com.projects.BookYourShow.backend.movie.dto.MovieRequest;
import com.projects.BookYourShow.backend.movie.dto.MovieResponse;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.movie.mapper.MovieMapper;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
    @Mock private MovieRepository repository;
    @Mock private MovieMapper mapper;
    @InjectMocks private MovieServiceImpl service;

    private MovieRequest request() { return new MovieRequest("Updated", Set.of(), 120.0, null); }
    @Test void getsEntityAndResponse() {
        Movie movie = new Movie(); MovieResponse response = mock(MovieResponse.class);
        when(repository.findById(1L)).thenReturn(Optional.of(movie));
        when(mapper.toResponse(movie)).thenReturn(response);
        assertSame(movie, service.getMovie(1L)); assertSame(response, service.findMovie(1L));
    }
    @Test void missingMoviesAreRejected() {
        assertThrows(ResourceNotFoundException.class, () -> service.getMovie(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.findMovie(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.updateMovie(1L, request()));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteMovie(1L));
        verify(repository, never()).save(any());
    }
    @Test void deletedMovieCannotBeReadOrUpdated() {
        Movie movie = new Movie(); movie.setDeleted(true);
        when(repository.findById(1L)).thenReturn(Optional.of(movie));
        assertThrows(RuntimeException.class, () -> service.findMovie(1L));
        assertThrows(RuntimeException.class, () -> service.updateMovie(1L, request()));
        verify(repository, never()).save(any());
    }
    @Test void createsUndeletedMovie() {
        Movie movie = new Movie(); movie.setDeleted(true); var request = request();
        when(mapper.toEntity(request)).thenReturn(movie); when(repository.save(movie)).thenReturn(movie);
        service.createMovie(request); assertFalse(movie.isDeleted()); verify(mapper).toResponse(movie);
    }
    @Test void updatesFieldsAndSoftDeletes() {
        Movie movie = new Movie(); when(repository.findById(1L)).thenReturn(Optional.of(movie));
        when(repository.save(movie)).thenReturn(movie);
        service.updateMovie(1L, request());
        assertEquals("Updated", movie.getTitle()); assertEquals(120.0, movie.getDuration());
        assertEquals(Set.of(), movie.getGenres()); assertNull(movie.getLanguage());
        service.deleteMovie(1L); assertTrue(movie.isDeleted()); verify(repository, times(2)).save(movie);
    }
    @Test void listsMappedMoviesAndEmptyResults() {
        Movie movie = new Movie(); MovieResponse response = mock(MovieResponse.class);
        when(repository.findAll()).thenReturn(List.of(movie), List.of()); when(mapper.toResponse(movie)).thenReturn(response);
        assertEquals(List.of(response), service.findAllMovie()); assertTrue(service.findAllMovie().isEmpty());
    }

}
