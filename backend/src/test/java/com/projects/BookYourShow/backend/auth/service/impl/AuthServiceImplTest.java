package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.dto.SignUpRequest;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;
import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import com.projects.BookYourShow.backend.user.service.RoleService;
import com.projects.BookYourShow.backend.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private RoleService roleService;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthServiceImpl authService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void signupGrantsOnlyUserRoleAndEncodesPassword() {
        Role role = new Role(UserRole.USER);
        when(roleService.getUserRole()).thenReturn(role);
        when(passwordEncoder.encode("password")).thenReturn("encoded");

        authService.signUp(new SignUpRequest("Customer", "customer@example.com", "password"));

        ArgumentCaptor<UserRequest> request = ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).createUser(request.capture());
        assertEquals(Set.of(role), request.getValue().roles());
        assertEquals("encoded", request.getValue().password());
        verify(roleService, never()).getAdminRole();
    }

    @Test
    void getCurrentUserRejectsMissingAndAnonymousAuthentication() {
        assertThrows(BadCredentialsException.class, authService::getCurrentUser);
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
        assertThrows(BadCredentialsException.class, authService::getCurrentUser);
    }

    @Test
    void getCurrentUserReturnsAuthenticatedUser() {
        User user = new User();
        user.setEnabled(true);
        SecurityUserDetails details = new SecurityUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));

        assertSame(user, authService.getCurrentUser());
    }
}
