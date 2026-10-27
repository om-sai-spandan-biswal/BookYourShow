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

import com.projects.BookYourShow.backend.theater.entity.Theater;
import com.projects.BookYourShow.backend.theater.repository.TheaterRepository;
import com.projects.BookYourShow.backend.theater.dto.TheaterRequest;
import com.projects.BookYourShow.backend.theater.dto.TheaterResponse;
import com.projects.BookYourShow.backend.user.entity.User;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.theater.mapper.TheaterMapper;
import com.projects.BookYourShow.backend.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
class TheaterServiceImplTest {
    @Mock private TheaterRepository repository;
    @Mock private TheaterMapper mapper;
    @Mock private AuthService auth;
    @InjectMocks private TheaterServiceImpl service;

    private TheaterRequest request() { return new TheaterRequest("Cinema", "Pune", "Main Road"); }
    @Test void listsTheaters() {
        Theater theater = new Theater(); TheaterResponse response = mock(TheaterResponse.class);
        when(repository.findAll()).thenReturn(List.of(theater), List.of()); when(mapper.toResponse(theater)).thenReturn(response);
        assertEquals(List.of(response), service.getTheaters()); assertTrue(service.getTheaters().isEmpty());
    }
    @Test void creationAssignsCurrentOwner() {
        Theater theater = new Theater(); User owner = new User(); var request = request();
        when(mapper.toEntity(request)).thenReturn(theater); when(auth.getCurrentUser()).thenReturn(owner);
        when(repository.save(theater)).thenReturn(theater); service.createTheater(request);
        assertSame(owner, theater.getOwner()); verify(mapper).toResponse(theater);
    }
    @Test void updatesTheaterFields() {
        Theater theater = new Theater(); when(repository.findById(1L)).thenReturn(Optional.of(theater));
        when(repository.save(theater)).thenReturn(theater); service.updateTheater(1L, request());
        assertEquals("Cinema", theater.getName()); assertEquals("Pune", theater.getCity()); assertEquals("Main Road", theater.getAddress());
    }
    @Test void activatesAndDeactivates() {
        Theater theater = new Theater(); when(repository.findById(1L)).thenReturn(Optional.of(theater));
        service.activateTheater(1L); assertTrue(theater.isActive());
        assertThrows(RuntimeException.class, () -> service.activateTheater(1L));
        service.deleteTheater(1L); assertFalse(theater.isActive()); service.deleteTheater(1L);
        verify(repository, times(3)).save(theater);
    }
    @Test void missingTheaterCannotBeChanged() {
        assertThrows(ResourceNotFoundException.class, () -> service.updateTheater(1L, request()));
        assertThrows(ResourceNotFoundException.class, () -> service.activateTheater(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteTheater(1L)); verify(repository, never()).save(any());
    }

}
