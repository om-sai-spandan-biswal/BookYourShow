package com.projects.BookYourShow.backend.user.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends RepositoryTestSupport {
    @Autowired UserRepository repository;

    @Test void findsExactEmailAndChecksExistence() {
        var user = user("customer@example.com"); em.flush(); em.clear();
        assertEquals(user.getId(),repository.findByEmail("customer@example.com").orElseThrow().getId());
        assertTrue(repository.existsByEmail("customer@example.com"));
        assertTrue(repository.findByEmail("missing@example.com").isEmpty()); assertFalse(repository.existsByEmail("missing@example.com"));
        assertFalse(repository.existsByEmail("CUSTOMER@example.com"));
    }
}
