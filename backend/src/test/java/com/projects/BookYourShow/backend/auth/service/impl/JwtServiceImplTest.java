package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.properties.JwtProperties;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;
import com.projects.BookYourShow.backend.user.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {
    private JwtServiceImpl service(long accessLifetime) {
        JwtServiceImpl service = new JwtServiceImpl(new JwtProperties(
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=", accessLifetime, 3600, "test"));
        service.init(); return service;
    }
    private SecurityUserDetails user(String email) {
        User user = new User(); user.setId(1L); user.setEmail(email); user.setEnabled(true);
        return new SecurityUserDetails(user);
    }
    @Test void accessTokenContainsIdentityAndCorrectType() {
        var service = service(600); var user = user("a@example.com"); String token = service.generateAccessToken(user);
        assertEquals(user.getUsername(), service.extractUsername(token)); assertTrue(service.isAccessToken(token));
        assertFalse(service.isRefreshToken(token)); assertFalse(service.isTokenExpired(token));
        assertTrue(service.isTokenValid(token,user)); assertFalse(service.isTokenValid(token,user("b@example.com")));
        assertNotNull(service.getJtiFromToken(token)); assertEquals(600,service.accessTokenExpiration());
    }
    @Test void refreshTokenPreservesJtiAndType() {
        var service = service(600); String token = service.generateRefreshToken(user("a@example.com"),"rotation-id");
        assertEquals("rotation-id",service.getJtiFromToken(token)); assertTrue(service.isRefreshToken(token));
        assertFalse(service.isAccessToken(token)); assertEquals(3600,service.refreshTokenExpiration());
    }
    @Test void malformedTamperedAndExpiredTokensAreRejected() {
        var service = service(600); assertThrows(JwtException.class, () -> service.extractUsername("invalid"));
        String token = service.generateAccessToken(user("a@example.com"));
        String[] parts = token.split("\\.");
        String tampered = parts[0] + "." + parts[1] + "." + (parts[2].startsWith("A") ? "B" : "A") + parts[2].substring(1);
        assertThrows(JwtException.class, () -> service.extractUsername(tampered));
        String expired = service(-60).generateAccessToken(user("a@example.com"));
        assertThrows(ExpiredJwtException.class, () -> service.isTokenExpired(expired));
    }
}
