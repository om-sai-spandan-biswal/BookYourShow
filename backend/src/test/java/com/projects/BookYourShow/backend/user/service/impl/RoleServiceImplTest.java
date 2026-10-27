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

import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.repository.RoleRepository;

import com.projects.BookYourShow.backend.user.enums.UserRole;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock private RoleRepository repository;
    @InjectMocks private RoleServiceImpl service;

    @Test void resolvesRolesByName() {
        Role admin = new Role(UserRole.ADMIN), user = new Role(UserRole.USER);
        when(repository.findByName(UserRole.ADMIN)).thenReturn(admin); when(repository.findByName(UserRole.USER)).thenReturn(user);
        assertSame(admin, service.getAdminRole()); assertSame(user, service.getUserRole());
    }

}
