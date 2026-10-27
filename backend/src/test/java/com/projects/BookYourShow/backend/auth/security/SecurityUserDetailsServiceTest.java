package com.projects.BookYourShow.backend.auth.security;

import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityUserDetailsServiceTest {
    private final UserRepository repository = mock(UserRepository.class);
    private final SecurityUserDetailsService service = new SecurityUserDetailsService(repository);
    @Test void loadsExistingUserByEmail() {
        User user = new User(); user.setEmail("a@example.com"); user.setEnabled(true);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        var details = (SecurityUserDetails) service.loadUserByUsername(user.getEmail());
        assertSame(user, details.user()); assertTrue(details.isEnabled()); assertEquals(user.getEmail(), details.getUsername());
    }
    @Test void rejectsUnknownEmail() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing@example.com"));
    }
}
