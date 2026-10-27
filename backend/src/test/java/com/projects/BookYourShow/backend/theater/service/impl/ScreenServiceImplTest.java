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
import com.projects.BookYourShow.backend.theater.entity.Theater;
import com.projects.BookYourShow.backend.theater.repository.ScreenRepository;
import com.projects.BookYourShow.backend.theater.repository.TheaterRepository;
import com.projects.BookYourShow.backend.theater.dto.ScreenRequest;
import com.projects.BookYourShow.backend.theater.dto.ScreenResponse;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.theater.mapper.ScreenMapper;

@ExtendWith(MockitoExtension.class)
class ScreenServiceImplTest {
    @Mock private ScreenRepository repository;
    @Mock private TheaterRepository theaters;
    @Mock private ScreenMapper mapper;
    @InjectMocks private ScreenServiceImpl service;

    @Test void getsScreenAndRejectsMissingScreen() {
        Screen screen = new Screen(); when(repository.findById(1L)).thenReturn(Optional.of(screen));
        assertSame(screen, service.getScreen(1L)); assertThrows(ResourceNotFoundException.class, () -> service.getScreen(2L));
    }
    @Test void listsScreensOfTheater() {
        Theater theater = new Theater(); Screen screen = new Screen(); ScreenResponse response = mock(ScreenResponse.class);
        when(theaters.findById(1L)).thenReturn(Optional.of(theater)); when(repository.findByTheater(theater)).thenReturn(List.of(screen));
        when(mapper.toResponse(screen)).thenReturn(response); assertEquals(List.of(response), service.getAllScreensOfTheater(1L));
    }
    @Test void createsInactiveScreenInTheater() {
        Theater theater = new Theater(); Screen screen = new Screen(); screen.setActive(true); var request = new ScreenRequest("IMAX");
        when(theaters.findById(1L)).thenReturn(Optional.of(theater)); when(mapper.toEntity(request)).thenReturn(screen);
        when(repository.save(screen)).thenReturn(screen); service.createScreen(1L, request);
        assertSame(theater, screen.getTheater()); assertFalse(screen.isActive()); verify(mapper).toResponse(screen);
    }
    @Test void updatesScreenName() {
        Screen screen = new Screen(); when(repository.findById(1L)).thenReturn(Optional.of(screen)); when(repository.save(screen)).thenReturn(screen);
        service.updateScreen(1L, new ScreenRequest("Updated")); assertEquals("Updated", screen.getName());
    }
    @Test void deletePersistsExistingActiveFlagBehavior() {
        Screen screen = new Screen(); when(repository.findById(1L)).thenReturn(Optional.of(screen)); when(repository.save(screen)).thenReturn(screen);
        service.deleteScreen(1L); assertTrue(screen.isActive()); verify(repository).save(screen);
    }
    @Test void rejectsMissingParentsAndScreens() {
        assertThrows(ResourceNotFoundException.class, () -> service.getAllScreensOfTheater(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.createScreen(1L, new ScreenRequest("A")));
        assertThrows(ResourceNotFoundException.class, () -> service.updateScreen(1L, new ScreenRequest("A")));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteScreen(1L)); verify(repository, never()).save(any());
    }

}
