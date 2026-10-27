package com.projects.BookYourShow.backend.user.service.impl;

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

import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.repository.UserRepository;
import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.user.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @InjectMocks private UserServiceImpl service;

    private UserRequest request() { return new UserRequest("New name", "a@example.com", "encoded", Set.of(), true); }
    @Test void createsUser() {
        var request = request(); User user = new User(); UserResponse response = mock(UserResponse.class);
        when(mapper.toEntity(request)).thenReturn(user); when(repository.save(user)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(response);
        assertSame(response, service.createUser(request)); verify(repository).existsByEmail(request.email());
    }
    @Test void duplicateEmailDoesNotSave() {
        when(repository.existsByEmail(request().email())).thenReturn(true);
        assertThrows(ResourceNotFoundException.class, () -> service.createUser(request()));
        verify(repository, never()).save(any()); verifyNoInteractions(mapper);
    }
    @Test void readsUserByEmailAndId() {
        User user = new User(); UserResponse response = mock(UserResponse.class);
        when(repository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(repository.findById(1L)).thenReturn(Optional.of(user)); when(mapper.toResponse(user)).thenReturn(response);
        assertSame(user, service.getUserByEmail("a@example.com")); assertSame(response, service.getUserById(1L));
    }
    @Test void missingUserIsRejected() {
        assertThrows(ResourceNotFoundException.class, () -> service.getUserByEmail("missing"));
        assertThrows(ResourceNotFoundException.class, () -> service.getUserById(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.updateUserById(1L, request()));
    }
    @Test void updatesNameAndPreservesCredentials() {
        User user = new User(); user.setPassword("old"); user.setEmail("original@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(user)); when(repository.save(user)).thenReturn(user);
        service.updateUserById(1L, request()); assertEquals("New name", user.getName());
        assertEquals("old", user.getPassword()); assertEquals("original@example.com", user.getEmail()); verify(repository).save(user);
    }

}
