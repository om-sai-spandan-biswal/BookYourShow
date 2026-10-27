package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.dto.LoginRequest;
import com.projects.BookYourShow.backend.auth.entity.RefreshToken;
import com.projects.BookYourShow.backend.auth.properties.JwtCookieProperties;
import com.projects.BookYourShow.backend.auth.repository.RefreshTokenRepository;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetailsService;
import com.projects.BookYourShow.backend.auth.service.JwtService;
import com.projects.BookYourShow.backend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.*;
import org.springframework.security.authentication.*;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceImplTest {
    @Mock SecurityUserDetailsService users;
    @Mock JwtService jwt;
    @Mock AuthenticationManager authentication;
    @Mock RefreshTokenRepository tokens;
    AuthServiceImpl service;
    private User user;
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    @BeforeEach void setup() {
        service = new AuthServiceImpl(users,new JwtCookieProperties("refresh",true,true,"Strict"),jwt,null,null,null,authentication,tokens);
        user = new User(); user.setEmail("a@example.com"); user.setEnabled(true);
    }
    private MockHttpServletRequest request() {
        var request = new MockHttpServletRequest(); request.setCookies(new Cookie("refresh","refresh-token")); return request;
    }
    private RefreshToken stored() {
        return RefreshToken.builder().user(user).jti("old").revoked(false).expiredAt(Instant.now().plusSeconds(600)).build();
    }
    private void recognizes(RefreshToken token) {
        when(jwt.isRefreshToken("refresh-token")).thenReturn(true);
        when(jwt.getJtiFromToken("refresh-token")).thenReturn("old");
        when(jwt.extractUsername("refresh-token")).thenReturn(user.getEmail());
        when(tokens.findByJti("old")).thenReturn(Optional.of(token));
    }
    @Test void loginAuthenticatesAndPersistsRefreshToken() {
        var details = new SecurityUserDetails(user); when(users.loadUserByUsername(user.getEmail())).thenReturn(details);
        when(jwt.refreshTokenExpiration()).thenReturn(3600L); when(jwt.generateAccessToken(details)).thenReturn("access");
        when(jwt.generateRefreshToken(eq(details),anyString())).thenReturn("refresh");
        var result = service.login(new LoginRequest(user.getEmail(),"password"));
        assertEquals("access",result.accessToken()); assertEquals("refresh",result.refreshToken());
        verify(authentication).authenticate(argThat(a -> a.getName().equals(user.getEmail()) && a.getCredentials().equals("password")));
        verify(tokens).save(argThat(t -> t.getUser() == user && !t.getRevoked() && t.getExpiredAt().isAfter(t.getCreatedAt()) && t.getJti()!=null));
    }
    @Test void loginRejectsBadCredentialsAndDisabledUser() {
        when(authentication.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest(user.getEmail(),"bad")));
        verifyNoInteractions(tokens);
    }
    @Test void loginRejectsDisabledUser() {
        user.setEnabled(false); when(users.loadUserByUsername(user.getEmail())).thenReturn(new SecurityUserDetails(user));
        assertThrows(DisabledException.class, () -> service.login(new LoginRequest(user.getEmail(),"password"))); verifyNoInteractions(tokens);
    }
    @Test void refreshRequiresCookieAndRefreshTokenType() {
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(new MockHttpServletRequest(),response));
        var blank = new MockHttpServletRequest(); blank.setCookies(new Cookie("refresh"," "));
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(blank,response));
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(request(),response)); verifyNoInteractions(tokens);
    }
    @Test void refreshRejectsUnrecognizedToken() {
        when(jwt.isRefreshToken("refresh-token")).thenReturn(true); when(jwt.getJtiFromToken("refresh-token")).thenReturn("old");
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(request(),response)); verify(tokens,never()).save(any());
    }
    @Test void refreshRejectsRevokedExpiredAndWrongOwnerTokens() {
        var stored = stored(); recognizes(stored); stored.setRevoked(true);
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(request(),response));
        stored.setRevoked(false); stored.setExpiredAt(Instant.now().minusSeconds(60));
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(request(),response));
        stored.setExpiredAt(Instant.now().plusSeconds(600)); when(jwt.extractUsername("refresh-token")).thenReturn("other@example.com");
        assertThrows(BadCredentialsException.class, () -> service.refreshToken(request(),response)); verify(tokens,never()).save(any());
    }
    @Test void refreshRotatesTokenAndRevokesPreviousToken() {
        var stored = stored(); recognizes(stored); var details = new SecurityUserDetails(user);
        when(users.loadUserByUsername(user.getEmail())).thenReturn(details); when(jwt.refreshTokenExpiration()).thenReturn(3600L);
        when(jwt.generateAccessToken(details)).thenReturn("new-access"); when(jwt.generateRefreshToken(eq(details),anyString())).thenReturn("new-refresh");
        var result = service.refreshToken(request(),response);
        assertEquals("new-access",result.accessToken()); assertEquals("new-refresh",result.refreshToken()); assertTrue(stored.getRevoked());
        assertNotNull(stored.getReplacedByJti()); verify(tokens).save(argThat(t -> t.getJti().equals(stored.getReplacedByJti()) && t.getUser()==user && !t.getRevoked()));
    }
    @Test void logoutRevokesRecognizedRefreshToken() {
        var stored = stored(); when(jwt.isRefreshToken("refresh-token")).thenReturn(true);
        when(jwt.getJtiFromToken("refresh-token")).thenReturn("old"); when(tokens.findByJti("old")).thenReturn(Optional.of(stored));
        service.logout(request(),response); assertTrue(stored.getRevoked()); assertFalse(stored.getExpiredAt().isAfter(Instant.now())); verify(tokens).save(stored);
    }
    @Test void logoutWithoutTokenOrWithInvalidTokenDoesNothing() {
        service.logout(new MockHttpServletRequest(),response); service.logout(request(),response); verifyNoInteractions(tokens);
    }
}
