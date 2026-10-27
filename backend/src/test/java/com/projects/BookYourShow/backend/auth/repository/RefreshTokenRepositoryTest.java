package com.projects.BookYourShow.backend.auth.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRepositoryTest extends RepositoryTestSupport {
    @Autowired RefreshTokenRepository repository;

    @Test void findsExactJtiAndReturnsEmptyForMissingToken() {
        var user = user("customer@example.com"); var now = java.time.Instant.now();
        var token = persist(com.projects.BookYourShow.backend.auth.entity.RefreshToken.builder().user(user).jti("token-id")
            .createdAt(now).expiredAt(now.plusSeconds(3600)).revoked(false).build());
        em.flush(); em.clear();
        assertEquals(token.getId(),repository.findByJti("token-id").orElseThrow().getId());
        assertTrue(repository.findByJti("missing-id").isEmpty());
    }
}
